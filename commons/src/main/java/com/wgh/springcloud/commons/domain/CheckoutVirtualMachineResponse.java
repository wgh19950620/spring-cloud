package com.wgh.springcloud.commons.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

/**
 * 云主机检验数据封装
 *
 * @author wangguanghui
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CheckoutVirtualMachineResponse extends ResourceResponse {

    /**
     * 校验出错数据
     */
    private List<CtYunVmAndLocalVmDifferences> ctYunVmAndLocalVmDifferences;

    /**
     * 接口调用异常的结果集
     */
    private Set<String> failBizIds;

    /**
     * 查询失败的结果集
     */
    private Set<String> invalidBizIds;
}
