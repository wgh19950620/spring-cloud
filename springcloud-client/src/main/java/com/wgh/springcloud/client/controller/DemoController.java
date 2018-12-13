package com.wgh.springcloud.client.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import com.wgh.springcloud.commons.util.UrlUtil;

/**
 * spring cloud client demo controller
 *
 * @author wangguanghui
 */
@RestController
public class DemoController {

    @Autowired
    DiscoveryClient discoveryClient;

    @Autowired
    @Qualifier("restTemplate")
    private RestTemplate restTemplate;

    @Autowired
    @Qualifier("loadBalanced")
    private RestTemplate loadBalanced;

    @Autowired
    private UrlUtil urlUtil;

    @GetMapping("/dc")
    public String dc() {
        String services = "services：" + discoveryClient.getServices();
        System.out.println(services);
        return services;
    }

    @GetMapping("/hello/restTemplate")
    public String queryUserByRT() {
        URI server = urlUtil.getServiceUrl("spring-cloud-customer", "error reuest url!");
        String name = "祝国龙";
        String url = server + "/hello/" + name;

        return restTemplate.getForObject(url, String.class);
    }

    @GetMapping("/hello/loadBalanced")
    public String queryUserByLB() {
        String name = "祝国龙";
        String url = "http://spring-cloud-customer/hello/" + name;

        return loadBalanced.getForObject(url, String.class);
    }

}
