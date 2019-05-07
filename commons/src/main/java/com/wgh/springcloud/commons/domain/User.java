package com.wgh.springcloud.commons.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * user domain
 *
 * @author wangguanghui
 */
@Data
@ApiModel(value = "用户对象", description = "用户对象")
public class User implements Serializable {

    private static final long serialVersionUID = -3946734305303957850L;

    @ApiModelProperty(value = "用户id")
    private Integer id;

    @ApiModelProperty(value = "用户姓名")
    private String name;

    @ApiModelProperty(value = "用户年龄", example = "(0,110]")
    private Integer age;

    @ApiModelProperty(value = "用户身份")
    private String identity;

    public User() {

    }

    public User(Integer id, String name, Integer age, String identity) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.identity = identity;
    }

}
