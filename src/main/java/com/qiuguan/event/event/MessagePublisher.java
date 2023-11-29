package com.qiuguan.event.event;

import com.qiuguan.event.client.MnsClient;

/**
 * @author fu yuan hui
 * @date 2023-11-29 12:15:19 Wednesday
 */
public interface MessagePublisher {

    @Deprecated
    void publish(MnsClient mnsClient, String queueName);

    void publish();
}
