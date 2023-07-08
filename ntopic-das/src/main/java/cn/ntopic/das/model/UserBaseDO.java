/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2021 All Rights Reserved.
 */
package cn.ntopic.das.model;

import cn.ntopic.core.builder.ToString;

import javax.annotation.Generated;

import javax.validation.constraints.Size;

import javax.validation.constraints.NotNull;


/**
 * MyBatis数据表对象
 *
 * 数据表: nt_user_base
 *
 * 特别注意：本文件由代码生成器自动生成，请勿直接修改！
 */
@Generated("https://gitee.com/obullxl/ntopic-boot")
public class UserBaseDO extends ToString {

    /* 数据表名 */
    public static final String TABLE_NAME = "nt_user_base";

    // ~~~~~~~~~~~~~~~~ 表字段常量 ~~~~~~~~~~~~~~~~~ //

    public static final String C_ID = "id";
    public static final String C_NAME = "name";
    public static final String C_PASSWORD = "password";
    public static final String C_ROLE_LIST = "role_list";
    public static final String C_EXT_MAP = "ext_map";
    public static final String C_CREATE_TIME = "create_time";
    public static final String C_MODIFY_TIME = "modify_time";

    // ~~~~~~~~~~~~~~~~ 属性名常量 ~~~~~~~~~~~~~~~~~ //

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String PASSWORD = "password";
    public static final String ROLE_LIST = "roleList";
    public static final String EXT_MAP = "extMap";
    public static final String CREATE_TIME = "createTime";
    public static final String MODIFY_TIME = "modifyTime";

    // ~~~~~~~~~~~~~~~~ 表字段列表 ~~~~~~~~~~~~~~~~~ //

    @NotNull
    @Size(max=64)
    private String id;

    @NotNull
    @Size(max=128)
    private String name;

    @NotNull
    @Size(max=64)
    private String password;

    @Size(max=256)
    private String roleList;

    @Size(max=4096)
    private String extMap;

    @NotNull
    private java.util.Date createTime;

    @NotNull
    private java.util.Date modifyTime;


    // ~~~~~~~~~~~~~~~~ 表字段访问器 ~~~~~~~~~~~~~~~~~ //

    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getRoleList() {
        return this.roleList;
    }
    public void setRoleList(String roleList) {
        this.roleList = roleList;
    }

    public String getExtMap() {
        return this.extMap;
    }
    public void setExtMap(String extMap) {
        this.extMap = extMap;
    }

    public java.util.Date getCreateTime() {
        return this.createTime;
    }
    public void setCreateTime(java.util.Date createTime) {
        this.createTime = createTime;
    }

    public java.util.Date getModifyTime() {
        return this.modifyTime;
    }
    public void setModifyTime(java.util.Date modifyTime) {
        this.modifyTime = modifyTime;
    }

}
