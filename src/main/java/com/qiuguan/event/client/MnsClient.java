package com.qiuguan.event.client;

import com.aliyun.mns.client.CloudQueue;
import com.aliyun.mns.client.MNSClient;

/**
 * @author fu yuan hui
 * @date 2023-11-28 17:05:11 Tuesday
 */
public interface MnsClient {

    CloudQueue getQueueRef(String queueName);

    MNSClient getClient();
}
