package com.qiuguan.event.handler;

import com.aliyun.mns.model.Message;

/**
 * @author fu yuan hui
 * @date 2023-11-29 12:23:02 Wednesday
 */
public interface MessageHandler {

    default boolean match(String queue) {
        return true;
    }

    void handle(Message message);
}
