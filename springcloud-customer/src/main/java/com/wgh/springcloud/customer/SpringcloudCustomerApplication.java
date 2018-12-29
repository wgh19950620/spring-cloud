package com.wgh.springcloud.customer;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * spring cloud customer application
 *
 * @author wangguanghui
 */
@SpringBootApplication(scanBasePackages = "com.wgh.springcloud")
@EnableEurekaClient
@EnableDiscoveryClient
@EnableFeignClients
@MapperScan("com.wgh.springcloud")
@EnableSwagger2
public class SpringcloudCustomerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringcloudCustomerApplication.class, args);
    }

    @Bean
    @Primary
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    @LoadBalanced
    public RestTemplate loadBalanced() {
        return new RestTemplate();
    }
}
