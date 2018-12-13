package com.wgh.springcloud.customer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import com.wgh.springcloud.commons.domain.User;
import com.wgh.springcloud.customer.dao.UserMapper;

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
