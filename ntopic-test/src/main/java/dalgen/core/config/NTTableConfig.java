/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2021 All Rights Reserved.
 */
package dalgen.core.config;

import dalgen.core.NTGenUtils;
import cn.ntopic.core.builder.ToString;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据表配置
 *
 * @author obullxl 2021年06月12日: 新增
 */
public class NTTableConfig extends ToString {
    private String tableName;
    private String aliasName;
    private String objectName;

    /* 自定义数据类型 */
    private List<String> userTypes;

    /* DO对象基类 */
    private String dtoBaseClass;

    /* 所有可用字段 */
    private List<NTFieldConfig> fields;

    /**
     * 配置检测
     */
    public void checkExecute(int idxNo) {
        if (StringUtils.isBlank(this.tableName)) {
            NTGenUtils.output("+ 数据表(" + idxNo + ")配置参数为空[tableName].");
            throw new RuntimeException("数据表(" + idxNo + ")配置参数为空[tableName]");
        }

        if (StringUtils.isBlank(this.objectName)) {
            NTGenUtils.output("+ 数据表(" + idxNo + ")配置参数为空[objectName].");
            throw new RuntimeException("数据表(" + idxNo + ")配置参数为空[objectName]");
        }
    }

    /**
     * WEB-获取Bean名称
     */
    public String findBeanName() {
        return StringUtils.uncapitalize(this.getObjectName());
    }

    /**
     * WEB-检测DO基类
     */
    public boolean checkBaseClass() {
        return StringUtils.isNotBlank(this.getDtoBaseClass());
    }

    /**
     * WEB-检测NotNull注解
     */
    public boolean checkNotNull() {
        for (NTFieldConfig field : this.getFields()) {
            if (field.checkNotNull()) {
                return true;
            }
        }

        return false;
    }

    /**
     * WEB-检测Size注解
     */
    public boolean checkMaxSize() {
        for (NTFieldConfig field : this.getFields()) {
            if (field.checkMaxSize()) {
                return true;
            }
        }

        return false;
    }

    /**
     * WEB-检测数值类型
     */
    public boolean checkNumberPath() {
        for (NTFieldConfig field : this.getFields()) {
            if (field.checkNumberType()) {
                return true;
            }
        }

        return false;
    }

    /**
     * WEB-检测字符串类型
     */
    public boolean checkStringPath() {
        for (NTFieldConfig field : this.getFields()) {
            if (field.checkStringType()) {
                return true;
            }
        }

        return false;
    }

    /**
     * WEB-检测日志类型
     */
    public boolean checkDateTimePath() {
        for (NTFieldConfig field : this.getFields()) {
            if (field.checkDateTimeType()) {
                return true;
            }
        }

        return false;
    }

    /**
     * WEB-检测自定义类型
     */
    public boolean checkSimplePathType() {
        for (NTFieldConfig field : this.getFields()) {
            if (field.checkSimplePathType()) {
                return true;
            }
        }

        return false;
    }

    // ~~~~~~~~~~~~~~~~~ getters and setters ~~~~~~~~~~~~~~~~~~~ //

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getAliasName() {
        if (StringUtils.isBlank(this.aliasName)) {
            return this.tableName;
        }

        return this.aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public List<String> getUserTypes() {
        if (this.userTypes == null) {
            this.userTypes = new ArrayList<>();
        }

        return this.userTypes;
    }

    public void setUserTypes(List<String> userTypes) {
        this.userTypes = userTypes;
    }

    public String getDtoBaseClass() {
        return dtoBaseClass;
    }

    public void setDtoBaseClass(String dtoBaseClass) {
        this.dtoBaseClass = dtoBaseClass;
    }

    public List<NTFieldConfig> getFields() {
        if (this.fields == null) {
            this.fields = new ArrayList<>();
        }

        return this.fields;
    }

    public void setFields(List<NTFieldConfig> fields) {
        this.fields = fields;
    }

}
