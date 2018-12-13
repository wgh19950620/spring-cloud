package com.wgh.springcloud.client.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import com.wgh.springcloud.client.dao.UserMapper;
import com.wgh.springcloud.commons.domain.User;

/**
 * user service
 *
 * @author wangguanghui
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public List<User> findUser() {
        return userMapper.findUser();
    }
}
