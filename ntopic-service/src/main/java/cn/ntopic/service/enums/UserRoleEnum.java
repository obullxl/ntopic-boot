/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2021 All Rights Reserved.
 */
package cn.ntopic.service.enums;

import cn.ntopic.core.NTAuthX;
import cn.ntopic.core.NTEnum;

/**
 * 用户角色枚举
 *
 * @author obullxl 2021年06月19日: 新增
 */
public enum UserRoleEnum implements NTEnum {
    //
    USER(NTAuthX.ROLE_USER, "普通用户"),
    //
    ADMIN(NTAuthX.ROLE_ADMIN, "管理员"),
    //
    ;

    /**
     * 角色代码
     */
    private final String code;

    /**
     * 角色说明
     */
    private final String message;

    UserRoleEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 获取枚举
     */
    public static UserRoleEnum findEnum(String code) {
        return NTEnum.valueOfEnum(UserRoleEnum.class, code).orElse(USER);
    }

}
