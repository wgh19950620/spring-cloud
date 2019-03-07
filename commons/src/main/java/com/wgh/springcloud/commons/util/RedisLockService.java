package com.wgh.springcloud.commons.util;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.stereotype.Component;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;
import java.util.UUID;

/**
 * redis distributed lock
 *
 * @author wangguanghui
 */
@Component
public class RedisLockService {

    private Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public String lock(String lockName, Long acquireTimeout, Long timeout) {
        return lockWithTimeout(lockName, acquireTimeout, timeout);
    }

    /**
     * 获取锁
     *
     * @param lockName       锁名称
     * @param acquireTimeout 上锁超时时间
     * @param timeout        获取锁超时时间
     * @return 获取锁密钥
     */
    public String lockWithTimeout(String lockName, Long acquireTimeout, Long timeout) {

        String retIdentifier = null;
        RedisConnectionFactory connectionFactory = stringRedisTemplate.getConnectionFactory();
        RedisConnection connection = connectionFactory.getConnection();

        String identifier = UUID.randomUUID().toString();
        String lockKey = "lock:" + lockName;
        int lockExpire = (int) (timeout / 1000);
        long end = System.currentTimeMillis() + acquireTimeout;

        while (System.currentTimeMillis() < end) {

            if (connection.setNX(lockKey.getBytes(), identifier.getBytes())) {
                connection.expire(lockKey.getBytes(), lockExpire);
                retIdentifier = identifier;
                RedisConnectionUtils.releaseConnection(connection, connectionFactory);
                return retIdentifier;
            }

            // 返回-1 代表 key 没有设置超时时间，为key 设置一个超时时间
            if (connection.ttl(lockKey.getBytes()) == -1) {
                connection.expire(lockKey.getBytes(), lockExpire);
            }

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                logger.warn("获取到分布式锁：线程中断！");
                Thread.currentThread().interrupt();
            }

        }

        RedisConnectionUtils.releaseConnection(connection, connectionFactory);
        return retIdentifier;
    }

    /**
     * 释放锁
     *
     * @param lockName   锁名称
     * @param identifier 获取锁密钥
     * @return true/false
     */
    public boolean releaseLock(String lockName, String identifier) {
        if (StringUtils.isBlank(identifier)) {
            return false;
        }

        RedisConnectionFactory connectionFactory = stringRedisTemplate.getConnectionFactory();
        RedisConnection connection = connectionFactory.getConnection();
        String lockKey = "lock:" + lockName;
        boolean releaseFlag = false;

        while (true) {
            try {
                connection.watch(lockKey.getBytes());
                byte[] valueBytes = connection.get(lockKey.getBytes());

                if (valueBytes == null) {
                    connection.unwatch();
                    releaseFlag = false;
                    break;
                }

                String identifierValue = new String(valueBytes);

                if (identifier.equals(identifierValue)) {
                    connection.multi();
                    connection.del(lockKey.getBytes());
                    List<Object> results = connection.exec();

                    if (results == null) {
                        continue;
                    }
                    releaseFlag = true;
                }

                connection.unwatch();
                break;
            } catch (Exception e) {
                logger.warn("释放锁异常", e);
                e.printStackTrace();
            }
        }

        RedisConnectionUtils.releaseConnection(connection, connectionFactory);
        return releaseFlag;
    }
}
