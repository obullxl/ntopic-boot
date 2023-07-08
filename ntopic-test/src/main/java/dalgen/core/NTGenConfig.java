/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2021 All Rights Reserved.
 */
package dalgen.core;

import dalgen.core.config.NTTableConfig;
import cn.ntopic.core.builder.ToString;
import cn.ntopic.core.utils.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 代码生成配置信息
 *
 * @author obullxl 2021年06月12日: 新增
 */
public class NTGenConfig extends ToString {

    /* 配置路径 */
    private final String configPath;

    /* 根路径 */
    private String codePath;

    /* 代码路径 */
    private String codePackage;

    /* DO对象基类 */
    private String dtoBaseClass;

    /* 数据表配置 */
    private List<NTTableConfig> tables;

    /**
     * CTOR
     */
    public NTGenConfig(String configPath) {
        this.configPath = configPath;
    }

    /**
     * 配置检测
     */
    public void checkExecute() {
        if (StringUtils.isBlank(this.codePath)) {
            NTGenUtils.output("+ 代码根路径参数为空[codePath].");
            throw new RuntimeException("代码根路径参数为空[codePath]");
        }

        if (StringUtils.isBlank(this.codePackage)) {
            NTGenUtils.output("+ 代码Package参数为空[codePackage].");
            throw new RuntimeException("代码Package参数为空[codePackage]");
        }

        if (CollectionUtils.isEmpty(this.tables)) {
            NTGenUtils.output("+ 未配置待生成代码数据表[tables].");
            throw new RuntimeException("未配置待生成代码数据表[tables]");
        }

        int idxNo = 1;
        for (NTTableConfig table : this.tables) {
            table.checkExecute(idxNo++);
        }
    }

    // ~~~~~~~~~~~~~~~~~ getters and setters ~~~~~~~~~~~~~~~~~~~ //

    public String getConfigPath() {
        return configPath;
    }

    public String getCodePath() {
        return codePath;
    }

    public void setCodePath(String codePath) {
        this.codePath = codePath;
    }

    public String getCodePackage() {
        return codePackage;
    }

    public void setCodePackage(String codePackage) {
        this.codePackage = codePackage;
    }

    public String getDtoBaseClass() {
        return dtoBaseClass;
    }

    public void setDtoBaseClass(String dtoBaseClass) {
        this.dtoBaseClass = dtoBaseClass;
    }

    public List<NTTableConfig> getTables() {
        if(this.tables == null) {
            this.tables = new ArrayList<>();
        }

        return this.tables;
    }

    public void setTables(List<NTTableConfig> tables) {
        this.tables = tables;
    }

}
