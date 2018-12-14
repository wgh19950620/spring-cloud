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

    public User queryUserById(Integer id) {
        return userMapper.queryUserById(id);
    }

    public int deleteUserById(Integer id) {
        return userMapper.deleteUserById(id);
    }

    public void saveUser(User user) {
        userMapper.savaUser(user);
    }

    public User findUserByBean(User user) {
        return userMapper.findUserByBean(user);
    }
}
