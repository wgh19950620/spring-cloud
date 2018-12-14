package com.wgh.springcloud.client.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

import com.wgh.springcloud.commons.domain.User;

/**
 * user mapper
 *
 * @author wangguanghui
 */
@Mapper
public interface UserMapper {


    /**
     * query uesr
     *
     * @return user
     */
    List<User> findUser();

    /**
     * query user by id
     *
     * @param id id
     * @return user
     */
    User queryUserById(Integer id);

    /**
     * delete user by id
     *
     * @param id id
     * @return code
     */
    int deleteUserById(Integer id);

    /**
     * sava user
     *
     * @param user user
     */
    void savaUser(User user);

    /**
     * find user by userBean
     *
     * @param user user
     * @return user
     */
    User findUserByBean(User user);
}
