package com.wgh.springcloud.customer.configuration;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.wgh.springcloud.commons.domain.User;

/**
 * feign spring-cloud-client configuration
 *
 * @author wangguanghui
 */
@FeignClient(name = "spring-cloud-client", fallback = FeignClient.class)
public interface FeignClientConfig {

    /**
     * UserController port
     *
     * @param id id
     * @return result
     */
    @PostMapping("/query/user/{id}")
    User queryUserById(@PathVariable("id") Integer id);

    /**
     * query user
     *
     * @return result
     */
    @GetMapping("/query/user")
    User queryUser();
}
