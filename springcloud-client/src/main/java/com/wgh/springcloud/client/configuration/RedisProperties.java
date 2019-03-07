package com.wgh.springcloud.client.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * redis properties
 *
 * @author wangguanghui
 */
@Component
@ConfigurationProperties(prefix = "redis.cache")
@Data
public class RedisProperties {

    private Integer expireSeconds;

    private String clusterNodes;

    private Integer commandTimeout;
}
