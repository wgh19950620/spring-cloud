package com.wgh.springcloud.commons.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

/**
 * 查询vm信息响应
 *
 * @author wangguanghui
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VirtualMachineDetailResponse {

    /**
     * 主机真实ID（resVmId）
     */
    private String externalId;

    /**
     * 主机名称（vmName）
     */
    private String name;

    /**
     * 虚拟资源真实ID（workOrderResourceId）
     */
    private String externalResourceId;

    /**
     * bizId
     */
    private String bizId;

    /**
     * cpu
     */
    private String cpu;

    /**
     * 内存
     */
    private String memory;

    /**
     * 操作系统
     */
    private String osType;

    /**
     * 资源池
     */
    private String region;

    /**
     * 可用区ID
     */
    private String availabilityZone;

    /**
     * 电源状态（vmStatus）
     */
    private String powerState;

    /**
     * 带宽真实ID
     */
    private String networkExternalId;

    /**
     * 带宽大小
     */
    private String bandwidth;

}
