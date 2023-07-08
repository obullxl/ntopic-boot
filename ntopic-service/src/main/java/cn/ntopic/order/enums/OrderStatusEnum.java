/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2023 All Rights Reserved.
 */
package cn.ntopic.order.enums;

import java.util.Arrays;
import java.util.Optional;

/**
 * 订单状态枚举
 *
 * @author obullxl 2023年05月20日: 新增
 */
public enum OrderStatusEnum {

    UNKNOWN(0, "未知"),

    WAIT_PAY(3, "创建待支付"),

    CLOSED(5, "支付超时关闭"),

    FINISHED(9, "订单已完成"),

    ;

    /**
     * 状态代码
     */
    private final int code;

    /**
     * 状态描述
     */
    private final String message;

    OrderStatusEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 枚举转换
     */
    public static Optional<OrderStatusEnum> convert(int code) {
        return Arrays.stream(OrderStatusEnum.values()).filter(e -> code == e.getCode()).findFirst();
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
