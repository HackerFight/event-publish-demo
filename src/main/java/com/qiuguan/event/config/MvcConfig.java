package com.qiuguan.event.config;

import com.qiuguan.event.client.MnsClient;
import com.qiuguan.event.event.MnsMessagePublisher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

/**
 * @author fu yuan hui
 * @date 2023-11-29 11:52:12 Wednesday
 */
//@EnableConfigurationProperties(MnsProperties.class)
@Configuration
public class MvcConfig {

    //@Bean
    public CommandLineRunner commandLineRunner(MnsMessagePublisher publisher, MnsClient mnsClient) {
        return args -> publisher.publish(mnsClient, "fyh-mns-queue");
    }

    //@Bean
    public CommandLineRunner commandLineRunner(MnsMessagePublisher publisher) {
        return args -> publisher.publish();
    }
}
