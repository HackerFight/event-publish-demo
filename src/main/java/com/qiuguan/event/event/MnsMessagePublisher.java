package com.qiuguan.event.event;

import com.aliyun.mns.client.CloudQueue;
import com.aliyun.mns.common.ClientException;
import com.aliyun.mns.common.ServiceException;
import com.aliyun.mns.model.Message;
import com.qiuguan.event.client.MnsClient;
import com.qiuguan.event.client.MnsProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author fu yuan hui
 * @date 2023-11-29 12:14:26 Wednesday
 */
@Slf4j
@Component
public class MnsMessagePublisher implements MessagePublisher,  ApplicationEventPublisherAware {

    private ApplicationEventPublisher publisher;

    @Resource
    private MnsProperties mnsProperties;

    @Resource
    private MnsClient mnsClient;

    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher  = applicationEventPublisher;
    }

    @Override
    public void publish(MnsClient mnsClient, String queueName) {
        CloudQueue queueRef = mnsClient.getQueueRef(queueName);
        while (true) {
            Message message = queueRef.popMessage(30);
            if (null != message) {
                this.publisher.publishEvent(new MnsEvent(this, message, queueName));
            }
        }
    }

    @Override
    public void publish() {
        List<String> queues = this.mnsProperties.getQueue();
        if (CollectionUtils.isEmpty(queues)) {
            log.warn(">>>>>>>> not found mns queues, skip publishing");
            return;
        }

        queues.forEach(queue -> executorService.execute(() -> {
            CloudQueue queueRef = this.mnsClient.getQueueRef(queue);
            AtomicBoolean start = new AtomicBoolean(true);
            while (start.get()) {
                try {
                    Message message = queueRef.popMessage();
                    if (null != message) {
                        this.publisher.publishEvent(new MnsEvent(this, message, queue));
                        // 处理完消息后，别忘了删除消息，否则它会在下次你调用PopMessage的时候仍然会出现在队列中
                        //queueRef.deleteMessage(message.getReceiptHandle());
                    }
                } catch (Exception e) {
                    log.error("监听队列 [{}] 读取消息失败", queue, e);
                    start.set(false);
                }
            }
        }));
    }
}
