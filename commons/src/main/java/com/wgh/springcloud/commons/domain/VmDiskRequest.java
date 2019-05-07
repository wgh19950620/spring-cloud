package com.wgh.springcloud.commons.domain;

import lombok.Data;

/**
 * 云主机磁盘 excel 请求数据
 *
 * @author wangguanghui
 */
@Data
public class VmDiskRequest {

    /**
     * 朱主机ID
     */
    private String vmId;

    /**
     * 是否为系统磁盘
     */
    private Integer isOsDisk;

    /**
     * 类型
     */
    private String type;

    /**
     * 大小
     */
    private String size;

    /**
     * 名称
     */
    private String name;

}
