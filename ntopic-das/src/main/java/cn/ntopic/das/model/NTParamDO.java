/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2023 All Rights Reserved.
 */
package cn.ntopic.das.model;

import cn.ntopic.core.builder.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * 参数DO
 *
 * @author obullxl 2023年09月09日: 新增
 */
public class NTParamDO extends ToString {

    /**
     * ID
     */
    @Min(0)
    private long id;

    /**
     * 分类
     */
    @NotNull
    @Size(max = 64)
    private String category;

    /**
     * 模块
     */
    @NotNull
    @Size(max = 64)
    private String module;

    /**
     * 名称
     */
    @NotNull
    @Size(max = 64)
    private String name;

    /**
     * 内容
     */
    @NotNull
    @Size(max = 4096)
    private String content;

    /**
     * 创建时间
     */
    @NotNull
    private Date createTime;

    /**
     * 更新时间
     */
    @NotNull
    private Date modifyTime;

    // ~~~~~~~~~~~~~~~~ 表字段访问器 ~~~~~~~~~~~~~~~~~ //

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
