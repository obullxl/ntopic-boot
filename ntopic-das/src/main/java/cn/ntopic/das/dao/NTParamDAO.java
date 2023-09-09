/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2023 All Rights Reserved.
 */
package cn.ntopic.das.dao;

import cn.ntopic.das.model.NTParamDO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 参数DAO
 *
 * @author obullxl 2023年09月09日: 新增
 */
@Repository("ntParamDAO")
public interface NTParamDAO {

    /**
     * 根据分类查询参数列表
     */
    @Select("SELECT * FROM nt_param WHERE category=#{category,jdbcType=VARCHAR}")
    List<NTParamDO> selectByCategory(@Param("category") String category);

    /**
     * 根据分类+模块查询参数列表
     */
    @Select("SELECT * FROM nt_param WHERE category=#{category,jdbcType=VARCHAR} AND module=#{module,jdbcType=VARCHAR}")
    List<NTParamDO> selectByModule(@Param("category") String category, @Param("module") String module);

    /**
     * 根据分类+模块+参数名查询单个参数
     */
    @Select("SELECT * FROM nt_param WHERE category=#{category,jdbcType=VARCHAR} AND module=#{module,jdbcType=VARCHAR} AND name=#{name,jdbcType=VARCHAR}")
    NTParamDO selectByName(@Param("category") String category, @Param("module") String module, @Param("name") String name);
}
