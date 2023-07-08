/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2021 All Rights Reserved.
 */
package dalgen.core.config;

import com.google.common.base.Joiner;
import cn.ntopic.core.builder.ToString;
import org.apache.commons.lang3.StringUtils;

import java.sql.JDBCType;

/**
 * 表字段配置
 *
 * @author obullxl 2021年06月12日: 新增
 */
public class NTFieldConfig extends ToString {
    private transient NTTableConfig table;

    private int index;
    private String field;
    private String jdbcType;
    private String javaType;
    private String nullable;
    private int columnSize = -1;

    private String dtoValue;

    /**
     * JDBC类型
     */
    public JDBCType findJdbcTypeEnum() {
        return JDBCType.valueOf(StringUtils.upperCase(this.jdbcType));
    }

    /**
     * 类属性名
     */
    public String toJavaCode() {
        String[] items = StringUtils.split(this.field, "_");
        for (int i = 1; i < items.length; i++) {
            items[i] = StringUtils.capitalize(items[i]);
        }

        return Joiner.on("").join(items);
    }

    /**
     * 类常量名
     */
    public String toJavaConst() {
        return StringUtils.upperCase(this.field);
    }

    /**
     * Getter访问器方法
     */
    public String findGetterName() {
        return "get" + StringUtils.capitalize(this.toJavaCode());
    }

    /**
     * Setter设置器方法
     */
    public String findSetterName() {
        return "set" + StringUtils.capitalize(this.toJavaCode());
    }

    /**
     * 属性类型
     */
    public String findJavaType() {
        if (StringUtils.isNotBlank(this.javaType)) {
            return this.javaType;
        }

        JDBCType jdbc = this.findJdbcTypeEnum();
        switch (jdbc) {
            case BIT:
            case TINYINT:
            case SMALLINT:
            case INTEGER:
                return "Integer";
            case BIGINT:
                return "Long";
            case FLOAT:
            case DOUBLE:
                return "Double";
            case CHAR:
            case VARCHAR:
            case LONGVARCHAR:
                return "String";
            case DATE:
            case TIME:
            case TIMESTAMP:
                return "java.util.Date";
            default:
                break;
        }

        throw new RuntimeException("JDBC类型[" + this.jdbcType + "]无法映射到Java类型.");
    }

    /**
     * WEB-检测是否可空
     */
    public boolean checkNotNull() {
        return StringUtils.equalsIgnoreCase("N", this.getNullable());
    }

    /**
     * WEB-检测字段类型
     */
    private boolean checkJavaType(String type) {
        return StringUtils.equalsIgnoreCase(type, this.findJavaType());
    }

    /**
     * WEB-检测MAX注解
     */
    public boolean checkMaxSize() {
        return this.getColumnSize() > 0;
    }

    /**
     * 检测数字类型
     */
    public boolean checkNumberType() {
        return this.checkJavaType("Integer")
                || this.checkJavaType("Long")
                || this.checkJavaType("Double");
    }

    /**
     * 检测字符串类型
     */
    public boolean checkStringType() {
        return this.checkJavaType("String");
    }

    /**
     * 检测日期类型
     */
    public boolean checkDateTimeType() {
        return this.checkJavaType("java.util.Date");
    }

    /**
     * 检测自定义类型
     */
    public boolean checkSimplePathType() {
        return StringUtils.isNotBlank(this.javaType);
    }

    /**
     * 检测DTO默认值
     */
    public boolean checkDtoValue() {
        return StringUtils.isNotBlank(this.dtoValue);
    }

    /**
     * 设置表配置
     */
    public void table(NTTableConfig tableConfig) {
        this.table = tableConfig;
    }

    /**
     * 读取表配置
     */
    public NTTableConfig table() {
        return this.table;
    }

    // ~~~~~~~~~~~~~~~~~~~~ getters and setters ~~~~~~~~~~~~~~~~~~~~ //

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getJdbcType() {
        return jdbcType;
    }

    public void setJdbcType(String jdbcType) {
        this.jdbcType = jdbcType;
    }

    public String getJavaType() {
        return javaType;
    }

    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }

    public NTTableConfig getTable() {
        return table;
    }

    public void setTable(NTTableConfig table) {
        this.table = table;
    }


    public String getNullable() {
        return nullable;
    }

    public void setNullable(String nullable) {
        this.nullable = nullable;
    }

    public int getColumnSize() {
        return columnSize;
    }

    public void setColumnSize(int columnSize) {
        this.columnSize = columnSize;
    }

    public String getDtoValue() {
        return dtoValue;
    }

    public void setDtoValue(String dtoValue) {
        this.dtoValue = dtoValue;
    }

}
