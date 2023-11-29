package com.qiuguan.event.event;

import com.qiuguan.event.handler.MessageHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collection;

/**
 * @author fu yuan hui
 * @date 2023-11-29 12:12:53 Wednesday
 */
@Slf4j
@Component
public class MnsMessageListener implements BeanFactoryAware, SmartInitializingSingleton {

    private BeanFactory beanFactory;

    @Autowired(required = false)
    private Collection<MessageHandler> messageHandlers;

    @EventListener
    public void handleMessageEvent(MnsEvent messageEvent) {
        //log.info("【事件发布订阅】接收到的MNS消息：{}", messageEvent.getMessage());
        if (!CollectionUtils.isEmpty(this.messageHandlers)) {
            for (MessageHandler messageHandler : this.messageHandlers) {
                if (messageHandler.match(messageEvent.getQueue())) {
                    messageHandler.handle(messageEvent.getMessage());
                }
            }
        }
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void afterSingletonsInstantiated() {

    }
}
