package com.wgh.springcloud.commons.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * grade param
 *
 * @author wangguanghui
 */
@Data
@ApiModel
public class Grade implements Serializable {

    private static final long serialVersionUID = -3946734305303957850L;

    @ApiModelProperty(value = "班级id")
    private Integer id;

    @ApiModelProperty(value = "用户id")
    private Integer userId;

    @ApiModelProperty(value = "班级名称")
    private String classGrade;

    @ApiModelProperty(value = "科目")
    private String subject;

    @ApiModelProperty(value = "分数")
    private Long grade;

    @ApiModelProperty(value = "职务")
    private String duty;

    @ApiModelProperty(value = "测试时间")
    private Date testTime;
}
