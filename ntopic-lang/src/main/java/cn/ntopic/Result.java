/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2023 All Rights Reserved.
 */
package cn.ntopic;

import cn.ntopic.core.builder.ToString;

/**
 * 请求结果包装器
 *
 * @author obullxl 2023年05月20日: 新增
 */
public class Result<T> extends ToString {

    /**
     * 结果成功标识
     */
    private final boolean success;

    /**
     * 结果返回码（如失败错误码、幂等成功标识等）
     */
    private String code;

    /**
     * 结果返回描述
     */
    private String message;

    /**
     * 结果对象
     */
    private T resultObj;

    public Result(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
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

    public T getResultObj() {
        return resultObj;
    }

    public void setResultObj(T resultObj) {
        this.resultObj = resultObj;
    }
}
