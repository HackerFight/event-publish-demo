package com.qiuguan.event.event;

import com.aliyun.mns.model.Message;
import org.springframework.context.ApplicationEvent;

/**
 * @author fu yuan hui
 * @date 2023-11-29 12:11:09 Wednesday
 */
public class MnsEvent extends ApplicationEvent {

    private final Message message;

    private final String queue;

    public MnsEvent(Object source, Message message, String queue) {
        super(source);
        this.message = message;
        this.queue = queue;
    }

    public Message getMessage() {
        return message;
    }

    public String getQueue() {
        return queue;
    }
}
