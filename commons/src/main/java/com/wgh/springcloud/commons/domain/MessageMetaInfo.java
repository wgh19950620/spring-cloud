package com.wgh.springcloud.commons.domain;

import lombok.Data;

/**
 * 加密信息
 *
 * @author wangguanghui
 */
@Data
public class MessageMetaInfo {

    private String account;

    private String name;

    private Long time;

    private String hash;
}
