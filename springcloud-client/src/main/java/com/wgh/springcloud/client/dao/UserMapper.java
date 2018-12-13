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
}
