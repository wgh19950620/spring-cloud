package com.wgh.springcloud.commons.domain;

import lombok.Data;

/**
 * 云主机 excel 请求参数
 *
 * @author wangguanghui
 */
@Data
public class VmRequest {

    /**
     * 成员接入号
     */
    private String platformId;

    /**
     * cpu
     */
    private Integer cpu;

    /**
     * bizId
     */
    private String bizId;

    /**
     * 子网
     */
    private String subnet;

    /**
     * 内存
     */
    private Integer memory;

    /**
     * 数据磁盘1 大小
     */
    private String diskSize1;

    /**
     * 数据磁盘1 类型
     */
    private String diskType1;

    /**
     * 数据磁盘2 大小
     */
    private String diskSize2;

    /**
     * 数据磁盘2 类型
     */
    private String diskType2;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 系统磁盘大小
     */
    private String sysDiskSize;

    /**
     * 系统磁盘类型
     */
    private String sysDiskType;

}
