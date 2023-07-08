/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2021 All Rights Reserved.
 */
package cn.ntopic.core;

import cn.ntopic.core.builder.ToString;

/**
 * 外部请求参数基类
 *
 * @author obullxl 2021年06月05日: 新增
 */
public class NTRequest<T extends BaseReqVO> extends ToString {

    /**
     * 业务产品
     */
    private String bizProduct;

    /**
     * 业务场景
     */
    private String bizScene;

    /**
     * 子业务场景
     */
    private String subBizScene;

    /**
     * 业务动作
     */
    private String bizAction;

    /**
     * 请求主体
     */
    private String principal;

    /**
     * 请求日志ID
     */
    private String traceId;

    /**
     * 请求参数
     */
    private T data;

    // ~~~~~~~~~~~~~~~~ getters and setters ~~~~~~~~~~~~~~~~ //

    public String getBizProduct() {
        return bizProduct;
    }

    public void setBizProduct(String bizProduct) {
        this.bizProduct = bizProduct;
    }

    public String getBizScene() {
        return bizScene;
    }

    public void setBizScene(String bizScene) {
        this.bizScene = bizScene;
    }

    public String getSubBizScene() {
        return subBizScene;
    }

    public void setSubBizScene(String subBizScene) {
        this.subBizScene = subBizScene;
    }

    public String getBizAction() {
        return bizAction;
    }

    public void setBizAction(String bizAction) {
        this.bizAction = bizAction;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
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
