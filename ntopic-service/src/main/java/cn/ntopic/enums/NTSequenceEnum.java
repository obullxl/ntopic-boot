/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2021 All Rights Reserved.
 */
package cn.ntopic.enums;

import cn.ntopic.core.NTEnum;

/**
 * 序列枚举
 *
 * @author obullxl 2021年06月20日: 新增
 */
public enum NTSequenceEnum implements NTEnum {

    USER_BASE("nt-UserBase", 1L, 99999999L, 100, "用户基本信息"),

    ;

    /* 枚举代码 */
    private final String code;

    /* 序列最小值 */
    private final long minValue;

    /* 序列最大值 */
    private final long maxValue;

    /* 递增步长 */
    private final int incStep;

    /* 枚举描述 */
    private final String message;

    NTSequenceEnum(String code, long minValue, long maxValue, int incStep, String message) {
        this.code = code;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.incStep = incStep;
        this.message = message;
    }

    // ~~~~~~~~~~~~~~~~ getters and setters ~~~~~~~~~~~~~~~~ //

    public long getMinValue() {
        return minValue;
    }

    public long getMaxValue() {
        return maxValue;
    }

    public int getIncStep() {
        return incStep;
    }

}
