package com.qiuguan.event.client;

import com.alibaba.fastjson2.JSON;
import com.aliyun.mns.client.CloudAccount;
import com.aliyun.mns.client.CloudQueue;
import com.aliyun.mns.client.MNSClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * @author fu yuan hui
 * @date 2023-11-28 17:05:32 Tuesday
 *
 * 参考文档：
 * https://help.aliyun.com/zh/mns/user-guide/manage-topics-in-the-console?spm=a2c4g.11186623.0.0.49ec13e037i9xo
 */
@Slf4j
@Component
public class DefaultMnsClient implements MnsClient, InitializingBean, DisposableBean {

    private final MnsProperties mnsProperties;

    private MNSClient mnsClient;

    @Override
    public CloudQueue getQueueRef(String queueName) {
        return this.mnsClient.getQueueRef(queueName);
    }

    @Override
    public MNSClient getClient() {
        return this.mnsClient;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //如果nacos没有配置，mnsProperties对象也不会空，只是属性都是null, 其中boolean 默认是false
        if (!this.mnsProperties.isEnable()) {
            log.warn(">>>>>>>>>>>>>>>>>>>>>> mns client disabled !!!");
            return;
        }

        CloudAccount account = new CloudAccount(this.mnsProperties.getAccessKeyId(),
                this.mnsProperties.getAccessKeySecret(), this.mnsProperties.getEndpoint());
        this.mnsClient = account.getMNSClient();

        log.info(">>>>>> mns client init completed! properties: {}", JSON.toJSONString(this.mnsProperties));
    }

    public DefaultMnsClient(MnsProperties mnsProperties) {
        this.mnsProperties = mnsProperties;
    }

    @Override
    public void destroy() throws Exception {
        if (this.mnsClient != null) {
            this.mnsClient.close();
        }
    }
}
