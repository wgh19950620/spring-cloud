package com.wgh.springcloud.client.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wgh.springcloud.client.service.UserService;
import com.wgh.springcloud.commons.domain.User;
import com.wgh.springcloud.commons.util.RedisKeyPrefix;
import com.wgh.springcloud.commons.util.UrlUtil;

/**
 * User controller
 *
 * @author wangguanghui
 */
@RestController
@Api(description = "用户模型")
public class UserContorller {

    private Logger logger = LoggerFactory.getLogger(UserContorller.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private UrlUtil urlUtil;

    @Resource
    private RedisTemplate<String, User> redisTemplate;
    // private RedisTemplate<String, Object> redisTemplate;根据实际情况取泛型

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

    @ApiOperation(value = "query user for redis", notes = "query user for redis")
    @ApiImplicitParam(name = "id", value = "user id", required = true, dataType = "int", paramType = "path")
    @PostMapping("/query/user/{id}")
    public User queryUserById(@PathVariable("id") Integer id) {
        String key = RedisKeyPrefix.USER + id;
        // 判断缓存是否存在
        if (redisTemplate.hasKey(key)) {
            User user = redisTemplate.opsForValue().get(key);
            logger.info("从缓存中获取了用户!");
            return user;
        }
        // 从数据库获取，并存入缓存
        User user = userService.queryUserById(id);
        // 放入缓存，并设置缓存时间 set(Object key, Object value, Integer time, TimeUnit.SECONDS);
        redisTemplate.opsForValue().set(key, user);
        return user;
    }

    @ApiOperation(value = "delete user for redis", notes = "delete user for redis")
    @ApiImplicitParam(name = "id", value = "user id", required = true, dataType = "int", paramType = "path")
    @PostMapping("/delete/user/{id}")
    public void deleteUserById(@PathVariable("id")Integer id){
        logger.info("删除用户loading...");
        String key = RedisKeyPrefix.USER + id;
        int num = userService.deleteUserById(id);
        if(num == 1) {
            // 缓存存在，删除缓存
            if(redisTemplate.hasKey(key)) {
                redisTemplate.delete(key);
                logger.info("删除用户时候，从缓存中删除用户 >> " + id);
            }
        }
    }

    @ApiOperation(value = "添加用户信息", notes = "添加用户信息")
    @PostMapping("/insert/user")
    public void insertUser(@RequestBody User user) {
        logger.info("添加用户信息loading...");
        userService.saveUser(user);
        User existUser = userService.findUserByBean(user);
        String key = RedisKeyPrefix.USER + existUser.getId();
        if(!redisTemplate.hasKey(key)) {
            redisTemplate.opsForValue().set(key, existUser);
            logger.info("将新保存的用户存入缓存中!");
        }
    }

}
