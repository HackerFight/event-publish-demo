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

        queues.forEach(queueName -> executorService.execute(() -> {
            CloudQueue queue = mnsClient.getQueueRef(queueName);
            boolean canProcess = true;
            while (canProcess) {
                try {
                    Message message = queue.popMessage();
                    if (message == null){
                        log.warn("MNS队列：{} 没有可以消费的数据", queueName);
                        continue;
                    }

                    this.publisher.publishEvent(new MnsEvent(this, message, queueName));
                    queue.deleteMessage(message.getReceiptHandle());

                    log.info(">>>>>>> MNS队列消费成功, 并从队列中删除已消费的数据");
                } catch (Exception e) {
                    canProcess = false;
                    log.error("MNS订阅队列 = [{}] 的数据发生错误", queueName, e);
                }
            }
        }));
    }
}
