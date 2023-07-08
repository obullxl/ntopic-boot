/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2021 All Rights Reserved.
 */
package cn.ntopic.model;

import cn.ntopic.core.BaseVO;
import cn.ntopic.core.utils.JSONUtils;
import cn.ntopic.das.model.UserBaseDO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 用户基础模型
 *
 * @author obullxl 2021年06月19日: 新增
 */
public class UserBaseModel extends BaseVO {

    /**
     * 用户ID
     */
    private String id;

    /**
     * 用户名
     */
    private String name;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 用户角色
     */
    private List<String> roleList;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date modifyTime;

    /**
     * 对象转换
     */
    public UserBaseDO toDO() {
        UserBaseDO userDO = new UserBaseDO();
        userDO.setId(this.getId());
        userDO.setName(this.getName());
        userDO.setPassword(this.getPassword());
        userDO.setRoleList(JSONUtils.toString(this.getRoleList()));
        userDO.setExtMap(JSONUtils.toString(this.getExtMap()));
        userDO.setCreateTime(this.getCreateTime());
        userDO.setModifyTime(this.getModifyTime());

        return userDO;
    }

    /**
     * 对象转换
     */
    public static UserBaseModel from(UserBaseDO userDO) {
        if (userDO == null) {
            return null;
        }

        UserBaseModel userModel = new UserBaseModel();
        userModel.setId(userDO.getId());
        userModel.setName(userDO.getName());
        userModel.setPassword(userDO.getPassword());
        userModel.setRoleList(JSONUtils.makeListValues(userDO.getRoleList()));
        userModel.setExtMap(JSONUtils.makeMapValues(userDO.getExtMap()));
        userModel.setCreateTime(userDO.getCreateTime());
        userModel.setModifyTime(userDO.getModifyTime());

        return userModel;
    }

    // ~~~~~~~~~~~~~~~~ getters and setters ~~~~~~~~~~~~~~~~ //

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getRoleList() {
        if (this.roleList == null) {
            this.roleList = new ArrayList<>();
        }

        return this.roleList;
    }

    public void setRoleList(List<String> roleList) {
        this.roleList = roleList;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

}
