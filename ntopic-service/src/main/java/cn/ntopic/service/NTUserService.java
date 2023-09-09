/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2021 All Rights Reserved.
 */
package cn.ntopic.service;

import cn.ntopic.das.model.NTParamDO;
import cn.ntopic.enums.NTSequenceEnum;
import cn.ntopic.model.UserBaseModel;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

/**
 * 用户信息服务
 *
 * @author obullxl 2021年10月23日: 新增
 */
public interface NTUserService extends UserDetailsService {

    /**
     * 序列名称
     */
    String SEQ_NAME = NTSequenceEnum.USER_BASE.getCode();

    /**
     * 生成用户ID
     */
    String newUserID();

    /**
     * 新建用户基本信息
     */
    void create(UserBaseModel userModel);

    /**
     * 根据ID查询用户信息
     */
    Optional<UserBaseModel> findById(String id);

    /**
     * 根据名称查询用户信息
     */
    Optional<UserBaseModel> findByName(String name);

    /**
     * 获取用户模块参数
     */
    List<NTParamDO> findUserParamList();

}
