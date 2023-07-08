/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2021 All Rights Reserved.
 */
package cn.ntopic.core;

import cn.ntopic.core.builder.ToString;

/**
 * 外部返回结果基类
 *
 * @author obullxl 2021年06月05日: 新增
 */
public class NTResponse<T extends BaseRespVO> extends ToString {

    /**
     * 成功标识
     */
    private String success;

    /**
     * 结果码
     */
    private String code;

    /**
     * 结果描述
     */
    private String message;

    /**
     * 请求日志ID
     */
    private String traceId;

    /**
     * 请求参数
     */
    private T data;

    // ~~~~~~~~~~~~~~~~ getters and setters ~~~~~~~~~~~~~~~~ //

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
