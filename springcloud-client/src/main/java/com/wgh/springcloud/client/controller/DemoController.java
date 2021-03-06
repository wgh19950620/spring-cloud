package com.wgh.springcloud.client.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URI;

import com.wgh.springcloud.client.configuration.FeignCustomerConfiguration;
import com.wgh.springcloud.commons.util.UrlUtil;

/**
 * spring cloud client demo controller
 *
 * @author wangguanghui
 */
@RestController
@Api(description = "测试用例")
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

    @Autowired
    private FeignCustomerConfiguration feignCustomerConfiguration;

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

    @GetMapping("/hello/feign")
    public String hello() {
        String name = "祝国龙";
        return feignCustomerConfiguration.index(name);
    }

    @GetMapping("/redirectTwo")
    public ModelAndView redirect() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("forward:hello/loadBalanced");
        return modelAndView;
    }

    @GetMapping("/redirect")
    public void handleFoo(HttpServletResponse response) throws IOException {
        response.sendRedirect("/query/user");
    }

}
