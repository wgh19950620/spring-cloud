package com.wgh.springcloud.client.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wgh.springcloud.client.service.UserService;
import com.wgh.springcloud.commons.domain.User;
import com.wgh.springcloud.commons.util.UrlUtil;

/**
 * User controller
 *
 * @author wangguanghui
 */
@RestController
public class UserContorller {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private UrlUtil urlUtil;

    @ApiOperation(value = "query user springcloud test", notes = "query user springcloud test")
    @ApiResponse(code = 0, message = "success")
    @GetMapping("/query/user")
    public User queryUser() {
        URI server = urlUtil.getServiceUrl("spring-cloud-customer", "error reuest url!");
        User user = new User();
        user.setId(1);
        user.setName("zhangsan");
        user.setAge(18);

        Map<String, Object> requestEntity = new HashMap<>();
        requestEntity.put("id", 2);
        requestEntity.put("name", "lisi");
        requestEntity.put("age", 18);
        String url = server + "/query/user/{identity}";

        String identity = "student";
        return restTemplate.postForObject(url, requestEntity, User.class, identity);
    }

    @ApiOperation(value = "query user", notes = "query user")
    @ApiResponse(code = 0, message = "success")
    @GetMapping("/find/user")
    public List<User> findUser() {
        return userService.findUser();
    }

}
