package com.wgh.springcloud.client.configuration;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * feign spring-cloud-customer configuration
 *
 * @author wangguanghui
 */
@FeignClient(name = "spring-cloud-customer", fallback = FeignClient.class)
public interface FeignCustomerConfiguration {

    /**
     * spring-cloud-customer helloController port
     *
     * @param name param
     * @return result
     */
    @GetMapping(value = "/hello/{name}")
    String index(@PathVariable("name") String name);
}
