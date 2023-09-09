/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2023 All Rights Reserved.
 */
package cn.ntopic;

import cn.ntopic.das.dao.NTParamDAO;
import cn.ntopic.das.model.NTParamDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

/**
 * 参数工具类
 *
 * @author obullxl 2023年09月09日: 新增
 */
// @Component("ntParamUtils") SpringBoot优先初始化本类，因此无需增加注解
public class NTParamUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogConstants.DAS);

    /**
     * 系统参数DAO
     */
    private static NTParamDAO NT_PARAM_DAO;

    /**
     * 依赖注入
     */
    public NTParamUtils(@Qualifier("ntParamDAO") NTParamDAO ntParamDAO) {
        Assert.notNull(ntParamDAO, "NTParamDAO注入为NULL.");
        NT_PARAM_DAO = ntParamDAO;

        // 打印日志
        LOGGER.info("{}:初始化完成.", this.getClass().getName());
    }

    /**
     * 根据分类获取参数列表
     */
    public static List<NTParamDO> findList(String category) {
        Assert.hasText(category, "分类参数为空");
        return NT_PARAM_DAO.selectByCategory(category);
    }

    /**
     * 根据分类+模块获取参数列表
     */
    public static List<NTParamDO> findList(String category, String module) {
        Assert.hasText(category, "分类参数为空");
        Assert.hasText(module, "模块参数为空");
        return NT_PARAM_DAO.selectByModule(category, module);
    }

    /**
     * 根据分类+模块+名称获取参数列表
     */
    public static Optional<NTParamDO> find(String category, String module, String name) {
        Assert.hasText(category, "分类参数为空");
        Assert.hasText(module, "模块参数为空");
        Assert.hasText(name, "参数名参数为空");
        return Optional.ofNullable(NT_PARAM_DAO.selectByName(category, module, name));
    }

}
