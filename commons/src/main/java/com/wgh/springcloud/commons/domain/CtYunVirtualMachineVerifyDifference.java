package com.wgh.springcloud.commons.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * 天翼云底层云主机与本地差异返回结果集
 *
 * @author wangguanghui
 */
@Getter
@Setter
public class CtYunVirtualMachineVerifyDifference {

    /**
     * 云主机真实ID
     */
    private String resVmId;

    /**
     * 云主机名称
     */
    private String vmName;

    /**
     * 云主机实际状态
     */
    private String vmStatus;

    /**
     * 云主机系统类型
     */
    private String osStyle;

    /**
     * 资源池ID
     */
    private String regionId;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 子网ID
     */
    private String vlanId;

    /**
     * 创建时间
     */
    private Long createDate;

    /**
     * 资源池可用区ID
     */
    private String zoneId;

    /**
     * 资源池可用区名称
     */
    private String zoneName;

    /**
     * cpu
     */
    private Integer cpuNum;

    /**
     * 内存
     */
    private Integer memSize;

    /**
     * 是否冻结
     */
    private Integer isFreeze;
}
