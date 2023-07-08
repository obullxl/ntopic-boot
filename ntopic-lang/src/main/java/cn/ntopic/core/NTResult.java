/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2021 All Rights Reserved.
 */
package cn.ntopic.core;

import cn.ntopic.core.builder.ToString;

/**
 * 结果对象
 *
 * @author obullxl 2021年06月20日: 新增
 */
public final class NTResult<T extends BaseVO> extends ToString {

    /**
     * 成功标识
     */
    private boolean success;

    /**
     * 结果码
     */
    private String code;

    /**
     * 结果描述
     */
    private String message;

    /**
     * 结果对象
     */
    private T data;

    /**
     * 成功结果
     */
    public static <T extends BaseVO> NTResult<T> success(T data) {
        NTResult<T> ntResult = new NTResult<>();
        ntResult.setSuccess(true);
        ntResult.setData(data);

        return ntResult;
    }

    /**
     * 失败结果
     */
    public static <T extends BaseVO> NTResult<T> failure(NTEnum NTEnum) {
        return failure(NTEnum.getCode(), NTEnum.getMessage());
    }

    /**
     * 失败结果
     */
    public static <T extends BaseVO> NTResult<T> failure(NTEnum NTEnum, Object... args) {
        return failure(NTEnum.getCode(), String.format(NTEnum.getMessage(), args));
    }

    /**
     * 失败结果
     */
    public static <T extends BaseVO> NTResult<T> failure(String code, String message) {
        NTResult<T> ntResult = new NTResult<>();
        ntResult.setSuccess(false);
        ntResult.setCode(code);
        ntResult.setMessage(message);

        return ntResult;
    }

    // ~~~~~~~~~~~~~~~~ getters and setters ~~~~~~~~~~~~~~~~ //

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
