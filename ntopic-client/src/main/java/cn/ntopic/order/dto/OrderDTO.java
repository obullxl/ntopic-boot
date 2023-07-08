/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2023 All Rights Reserved.
 */
package cn.ntopic.order.dto;

import cn.ntopic.core.builder.ToString;

/**
 * 订单基本信息
 *
 * @author obullxl 2023年05月20日: 新增
 */
public class OrderDTO extends ToString {
    /**
     * 订单ID，非空
     */
    private String id;

    /**
     * 订单金额
     */
    private String amount;

    /**
     * 订单状态
     */
    private int status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
