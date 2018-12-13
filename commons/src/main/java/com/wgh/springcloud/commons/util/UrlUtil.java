package com.wgh.springcloud.commons.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Component;

import java.net.URI;

/**
 * get url address
 *
 * @author wangguanghui
 */
@Component
public class UrlUtil {
    private static final Logger LOG = LoggerFactory.getLogger(UrlUtil.class);

    private final LoadBalancerClient loadBalancer;

    @Autowired
    UrlUtil(LoadBalancerClient loadBalancer) {
        this.loadBalancer = loadBalancer;
    }

    /**
     * TODO: Complement this with a simpler version without fallback-url!
     *
     * @param serviceId   spring cloud name
     * @param fallbackUri return error message
     * @return url
     */
    public URI getServiceUrl(String serviceId, String fallbackUri) {
        URI uri;
        try {
            ServiceInstance instance = loadBalancer.choose(serviceId);
            uri = instance.getUri();
            LOG.debug("Resolved serviceId '{}' to URL '{}'.", serviceId, uri);
        } catch (RuntimeException e) {
            e.printStackTrace();
            uri = URI.create(fallbackUri);
            LOG.warn("Failed to resolve serviceId '{}'. Fallback to URL '{}'.", serviceId, uri);
        }
        return uri;
    }
}
