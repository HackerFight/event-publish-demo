package com.qiuguan.event.client;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author fu yuan hui
 * @date 2023-11-28 17:17:17 Tuesday
 *
 * {@link Component} 注解和 {@link org.springframework.boot.context.properties.EnableConfigurationProperties}
 * 注解二选一
 */
@Data
@Component
@ConfigurationProperties(prefix = "aliyun.mns")
public class MnsProperties {

    private String endpoint;

    private String accessKeyId;

    private String accessKeySecret;

    private List<String> queue;

    private String topic;

    /**
     * 自定义类属性
     */
    @NestedConfigurationProperty
    private Config config;

    @Data
    static class Config {

        private String id;
    }
}
