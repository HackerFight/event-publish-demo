package com.qiuguan.event.handler;

import com.aliyun.mns.model.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author fu yuan hui
 * @date 2023-11-29 13:56:36 Wednesday
 */
@Slf4j
@Component
public class DefaultMessageHandler implements MessageHandler {

    @Override
    public void handle(Message message) {
        log.info("默认的消息处理器处理消息：当前线程：{}, 当前时间：{} message = {}",
                Thread.currentThread().getName(),
                LocalDateTime.now(),
                message.getMessageBodyAsString());
    }
}
