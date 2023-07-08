/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2021 All Rights Reserved.
 */
package cn.ntopic.das.dao;

import cn.ntopic.das.model.UserBaseDO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author obullxl 2021年10月17日: 新增
 */
@Repository("userBaseDAO")
public interface UserBaseDAO {

    /**
     * 新增用户记录
     */
    @Insert("INSERT INTO nt_user_base (id, name, password, role_list, ext_map, create_time, modify_time)" +
            " VALUES (#{id,jdbcType=VARCHAR}, #{name,jdbcType=VARCHAR}, #{password,jdbcType=VARCHAR}, #{roleList,jdbcType=VARCHAR}, #{extMap,jdbcType=VARCHAR}, #{createTime,jdbcType=TIMESTAMP}, #{modifyTime,jdbcType=TIMESTAMP})")
    void insert(UserBaseDO userBaseDO);

    /**
     * 根据ID查询记录
     */
    @Select("SELECT * FROM nt_user_base WHERE id = #{id,jdbcType=VARCHAR}")
    UserBaseDO selectById(@Param("id") String id);

    /**
     * 根据名称查询记录
     */
    @Select("SELECT * FROM nt_user_base WHERE name = #{name,jdbcType=VARCHAR}")
    UserBaseDO selectByName(@Param("name") String name);

    /**
     * 查询所有用户
     */
    @Select("SELECT * FROM nt_user_base LIMIT 30")
    List<UserBaseDO> select();

    /**
     * 更新角色列表
     * FIXME: SQLite/MySQL 当前时间函数不一致，本次通过入参传入！
     */
    @Update("UPDATE nt_user_base SET modify_time=#{modifyTime,jdbcType=TIMESTAMP}, role_list=#{roleList,jdbcType=VARCHAR} WHERE id=#{id,jdbcType=VARCHAR}")
    int updateRoleList(@Param("id") String id, @Param("modifyTime") Date modifyTime, @Param("roleList") String roleList);

    /**
     * 删除用户记录
     */
    @Delete("DELETE FROM nt_user_base WHERE id = #{id,jdbcType=VARCHAR}")
    int deleteById(@Param("id") String id);

    /**
     * 删除用户记录
     */
    @Delete("DELETE FROM nt_user_base WHERE name = #{name,jdbcType=VARCHAR}")
    int deleteByName(@Param("name") String name);

}
