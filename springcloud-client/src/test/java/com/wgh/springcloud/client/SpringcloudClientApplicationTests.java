package com.wgh.springcloud.client;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.JedisCluster;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringcloudClientApplicationTests {

    private final String KEY = "zhangsan";

    @Resource
    private RedisTemplate<Object, Object> redisTemplate;

    @Autowired
    private JedisCluster jedisCluster;

    @Test
    public void contextLoads() {
    }

    @Test
    public void redisTest() {
        Long aLong = jedisCluster.incrBy(KEY, 1);
        Long incr = jedisCluster.incr(KEY);
        System.out.println(incr);
        System.out.println(aLong);
    }

}
