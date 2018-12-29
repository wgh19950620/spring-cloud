package com.wgh.springcloud.customer.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

import java.util.Arrays;
import java.util.List;

import com.wgh.springcloud.commons.domain.User;
import com.wgh.springcloud.customer.configuration.FeignClientConfig;

/**
 * user controller
 *
 * @author wangguanghui
 */
@RestController
@Api(description = "用户模型")
public class UserController {

    @Resource(name = "loadBalanced")
    private RestTemplate loadBalanced;

    @Autowired
    private FeignClientConfig feignClientConfig;

    @ApiOperation(value = "query user springcloud test", notes = "query user springcloud test")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "user", value = "用户信息", required = true, dataType = "User", paramType = "body"),
            @ApiImplicitParam(name = "identity", value = "身份", required = true, dataType = "string", paramType = "query")
    })
    @PostMapping("/query/user/{identity}")
    public User queryUser(@RequestBody User user, @PathVariable("identity") String identity) {

        System.out.println("User: " + user);
        System.out.println("identity: " + identity);

        return user;
    }

    @ApiOperation(value = "query user", notes = "query user")
    @ApiResponse(code = 0, message = "success")
    @GetMapping("/find/user")
    public List<User> findUser() {
        String url = "http://spring-cloud-client/find/user/";

        User[] users = loadBalanced.getForObject(url, User[].class);
        List<User> userList = Arrays.asList(users);
        userList.forEach(user -> {
            System.out.println(user.toString());
        });

        return userList;
    }

    @GetMapping("/query/user/feign")
    public User queryByFeign() {
        return feignClientConfig.queryUserById(1);
    }

    @GetMapping("/query/user")
    public User queryUser() {
        return feignClientConfig.queryUser();
    }

}
