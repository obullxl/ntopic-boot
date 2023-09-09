/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2021 All Rights Reserved.
 */
package cn.ntopic.service.impl;

import cn.ntopic.LogConstants;
import cn.ntopic.NTParamUtils;
import cn.ntopic.core.exception.NTBizException;
import cn.ntopic.das.dao.UserBaseDAO;
import cn.ntopic.das.model.NTParamDO;
import cn.ntopic.das.model.UserBaseDO;
import cn.ntopic.enums.NTErrorCodeEnum;
import cn.ntopic.model.UserBaseModel;
import cn.ntopic.service.NTUserService;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 用户服务
 *
 * @author obullxl 2021年06月19日: 新增
 */
@Component("ntUserService")
public final class NTUserServiceImpl implements NTUserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogConstants.BIZ);

    /**
     * 序列服务
     */
//    private final NTSequenceX ntSequenceX;

    /**
     * 用户基本信息DAO
     */
    public final UserBaseDAO userBaseDAO;

    /* 用户缓存 */
    private final LoadingCache<String, Optional<UserBaseModel>> cache;

    @Autowired
    public NTUserServiceImpl(@Value("${ntopic.biz.NTUserX.cache.maximum_size}") int maximumSize, @Value("${ntopic.biz.NTUserX.cache.expire_seconds}") int expireSeconds
            , /*NTSequenceX ntSequenceX, */@Qualifier("userBaseDAO") UserBaseDAO userBaseDAO) {
        this.cache = CacheBuilder.newBuilder()
                .maximumSize(maximumSize)
                .expireAfterWrite(expireSeconds, TimeUnit.SECONDS)
                .build(new CacheLoader<String, Optional<UserBaseModel>>() {
                    @Override
                    public Optional<UserBaseModel> load(String userName) {
                        return findByName(userName);
                    }
                });

//        this.ntSequenceX = ntSequenceX;
        this.userBaseDAO = userBaseDAO;

        // 打印日志
        LOGGER.info("{}:初始化完成.", this.getClass().getName());
    }

    /**
     * SpringSecurity加载用户
     */
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        if (StringUtils.isBlank(userName)) {
            throw new UsernameNotFoundException("NTUserX:用户不存在(" + userName + ")");
        }

        // 缓存获取
        Optional<UserBaseModel> optUserModel;
        try {
            optUserModel = this.cache.get(userName);
        } catch (ExecutionException e) {
            throw new RuntimeException("查询用户异常(" + userName + ")", e);
        }

        if (!optUserModel.isPresent()) {
            throw new UsernameNotFoundException("NTUserX:用户不存在(" + userName + ")");
        }

        UserBaseModel userModel = optUserModel.get();
        return new User(userModel.getName(), userModel.getName()
                , userModel.getRoleList().stream()
                .map(t -> "ROLE_" + t)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList()));
    }

    /**
     * 生成用户ID
     */
    @Override
    public String newUserID() {
//        return "T" + this.ntSequenceX.nextValue(SEQ_NAME);
        return "T" + RandomUtils.nextLong();
    }

    /**
     * 新建用户基本信息
     */
    @Override
    public void create(final UserBaseModel userModel) {
        // 存在检测
        if (this.findByName(userModel.getName()).isPresent()) {
            LOGGER.warn("NTUserX:用户名已存在-{}.", userModel);
            throw NTBizException.of(NTErrorCodeEnum.USER_NAME_EXIST, userModel.getName());
        }

        // 用户ID
        userModel.setId(this.newUserID());

        // TODO:用户密码
        userModel.setPassword(userModel.getId());

        // 新增模型
        userModel.setCreateTime(new Date());
        userModel.setModifyTime(new Date());
        try {
            this.userBaseDAO.insert(userModel.toDO());
        } catch (Throwable e) {
            LOGGER.error("NTUserX:创建用户异常-{}.", userModel, e);
            throw NTBizException.of(NTErrorCodeEnum.CREATE_USER_ERROR, userModel.getName());
        }
    }

    /**
     * 根据ID查询用户信息
     */
    @Override
    public Optional<UserBaseModel> findById(String id) {
        UserBaseDO userBaseDO = this.userBaseDAO.selectById(id);
        return Optional.ofNullable(userBaseDO).map(UserBaseModel::from);
    }

    /**
     * 根据名称查询用户信息
     */
    @Override
    public Optional<UserBaseModel> findByName(String name) {
        UserBaseDO userBaseDO = this.userBaseDAO.selectByName(name);
        return Optional.ofNullable(userBaseDO).map(UserBaseModel::from);
    }

    /**
     * 获取用户模块参数
     */
    @Override
    public List<NTParamDO> findUserParamList() {
        return NTParamUtils.findList("CONFIG", "USER");
    }
}
