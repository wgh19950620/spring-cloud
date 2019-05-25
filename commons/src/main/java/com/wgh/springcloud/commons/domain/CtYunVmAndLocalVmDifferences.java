package com.wgh.springcloud.commons.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * 天翼云底层云主机和本地云主机差异性
 *
 * @author wangguanghui
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CtYunVmAndLocalVmDifferences {

    private String bizId;

    private String region;

    private List<CtYunVirtualMachineVerifyDifference> ctYunVmDetails;

    private List<VirtualMachineDetailResponse> localVmDetails;
}
