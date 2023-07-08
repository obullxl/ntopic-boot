/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2023 All Rights Reserved.
 */
package cn.ntopic.order.model;

import cn.ntopic.order.dto.OrderDTO;
import cn.ntopic.order.enums.OrderStatusEnum;
import cn.ntopic.core.builder.ToString;
import org.springframework.util.Assert;

import java.math.BigDecimal;

/**
 * 订单对象
 *
 * @author obullxl 2023年05月20日: 新增
 */
public class OrderModel extends ToString {

    /**
     * 订单ID，非空
     */
    private String id;

    /**
     * 订单金额
     */
    private BigDecimal amount;

    /**
     * 订单状态
     */
    private OrderStatusEnum status;

    /**
     * 构建订单模型
     */
    public static OrderModel from(OrderDTO orderDTO) {
        Assert.notNull(orderDTO, "OrderDTO入参为NULL.");

        OrderModel orderModel = new OrderModel();
        orderModel.setId(orderDTO.getId());
        orderModel.setAmount(new BigDecimal(orderDTO.getAmount()));
        orderModel.setStatus(OrderStatusEnum.convert(orderDTO.getStatus()).orElse(OrderStatusEnum.UNKNOWN));

        return orderModel;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public OrderStatusEnum getStatus() {
        return status;
    }

    public void setStatus(OrderStatusEnum status) {
        this.status = status;
    }
}
