package com.wgh.springcloud.commons.domain;

import lombok.Data;

/**
 * 资源响应
 * <p>
 * 用于继承。包含状态和响应消息
 * </p>
 *
 * @author bizhennan
 */
@Data
public class ResourceResponse {
    /**
     * 返回状态
     */
    private Integer statusCode;

    /**
     * 消息
     */
    private String message;


    /**
     * 附加数据
     */
    private Object data;

    public ResourceResponse() {
        this.statusCode = StatusCode.SUCCESS;
        this.message = "";
    }

    public interface StatusCode {
        /**
         * 成功
         */
        int SUCCESS = 0;

        /**
         * 失败
         */
        int ERROR = 1;
    }

    public ResourceResponse(Object data) {
        this.data = data;
    }

    /**
     * 使用消息文本生成响应对象
     *
     * @param statusCode 编码
     * @param message    消息
     * @param data       附加对象数据
     */
    public ResourceResponse(Integer statusCode, String message, Object data) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }

    public void saveSuccess() {
        this.statusCode = ResourceResponse.StatusCode.SUCCESS;
    }

    /**
     * 返回成功后的封装数据
     *
     * @param data 附件对象数据
     * @return 响应数据
     */
    public ResourceResponse success(Object data) {
        this.statusCode = StatusCode.SUCCESS;
        this.message = "";
        this.data = data;

        return this;
    }

    public void saveError(String message) {
        this.statusCode = ResourceResponse.StatusCode.ERROR;
        this.message = message;
    }

    public void saveError() {
        this.statusCode = ResourceResponse.StatusCode.ERROR;
    }

    public ResourceResponse success() {
        this.statusCode = StatusCode.SUCCESS;
        this.message = "";

        return this;
    }

    public ResourceResponse fail(String message) {
        this.statusCode = ResourceResponse.StatusCode.ERROR;
        this.message = message;

        return this;
    }
}
