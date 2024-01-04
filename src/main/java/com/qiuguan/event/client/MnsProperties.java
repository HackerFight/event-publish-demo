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
 *
 *
 * 如果使用的是nacos, 可以搭配  @RefreshScope 注解来实现动态更新
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
     * 指定一个开关，来决定是否开启mns, 这个主要是我们一个服务，可以多次打包给外部公司，有的需要mns,有的不需要，所以我这就动态调整一下
     *
     * 默认是false, 如果nacos配置了true,这里就是true
     */
    private boolean enable = false;

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
