/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2021 All Rights Reserved.
 */
package cn.ntopic.enums;

import cn.ntopic.core.NTEnum;

/**
 * 错误码枚举
 *
 * @author obullxl 2021年06月20日: 新增
 */
public enum NTErrorCodeEnum implements NTEnum {

    USER_NAME_EXIST("USER_NAME_EXIST", "用户名已存在(%s)"),

    CREATE_USER_ERROR("CREATE_USER_ERROR", "创建用户失败(%s)")

    ;

    /**
     * 枚举编码
     */
    private final String code;

    /**
     * 枚举描述
     */
    private final String message;

    NTErrorCodeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

}
