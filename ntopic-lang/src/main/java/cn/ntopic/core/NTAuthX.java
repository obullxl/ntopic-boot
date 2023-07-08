/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2021 All Rights Reserved.
 */
package cn.ntopic.core;

/**
 * 认证信息
 *
 * @author obullxl 2021年06月19日: 新增
 */
public interface NTAuthX {

    /**
     * 角色：用户
     */
    String ROLE_USER = "USER";

    /**
     * 角色：管理员
     */
    String ROLE_ADMIN = "ADMIN";

    /**
     * Spring-EL：用户角色
     */
    String HAS_ROLE_USER = "hasRole('" + ROLE_USER + "')";

    /**
     * Spring-EL：管理员角色
     */
    String HAS_ROLE_ADMIN = "hasRole('" + ROLE_ADMIN + "')";

}
