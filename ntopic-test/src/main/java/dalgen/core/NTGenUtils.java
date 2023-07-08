/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2021 All Rights Reserved.
 */
package dalgen.core;

import dalgen.core.config.NTFieldConfig;
import dalgen.core.config.NTTableConfig;
import cn.ntopic.core.velocity.VelocityUtils;
import cn.ntopic.core.xml.XMLNode;
import cn.ntopic.core.xml.XMLUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.util.Assert;

import java.io.File;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.sql.JDBCType;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 代码生成工具类
 *
 * @author obullxl 2021年06月12日: 新增
 */
public abstract class NTGenUtils {

    /* 内容输出 */
    private final static PrintStream OUT = System.out;

    /* 编码 */
    public static final Charset CHARSET = Charset.forName("UTF-8");

    /* 配置路径内容 */
    public static final String CONFIG_PATH_KEY = "configPath";

    /* 代码路径 */
    public static final String CODE_PATH_KEY = "codePath";

    /* 代码Package */
    public static final String CODE_PACKAGE_KEY = "codePackage";

    /* DTO父类 */
    public static final String DTO_BASE_CLASS_KEY = "dtoBaseClass";

    /* 公用参数 */
    public static final Map<String, String> CONTEXT = new ConcurrentHashMap<>();

    /**
     * 内容输出-格式化
     */
    public static void output(String format, Object... arguments) {
        OUT.println(String.format(format, arguments));
    }

    /**
     * 执行参数映射
     * <p>
     * 如:
     * -DconfigType=FILE -DconfigPath=/home/shizihu/mybatis
     * <p>
     * 映射:
     * configType=FILE
     * configPath=/home/shizihu/mybatis
     */
    public static Map<String, String> makeExecArgsMap(List<String> args) {
        Map<String, String> values = new ConcurrentHashMap<>();

        if (CollectionUtils.isEmpty(args)) {
            return values;
        }

        for (String arg : args) {
            if (StringUtils.startsWith(arg, "-D")
                    && StringUtils.contains(arg, "=")) {
                String[] parts = StringUtils.split(arg, "=", 2);
                String key = StringUtils.substring(parts[0], 2);
                String value = StringUtils.trimToEmpty(parts[1]);

                values.put(key, value);
            }
        }

        return values;
    }

    /**
     * 初始化配置
     */
    @SuppressWarnings("unchecked")
    public static NTGenConfig makeConfig(Map<String, String> execArgs) {
        String configPath = MapUtils.getString(execArgs, CONFIG_PATH_KEY);

        // 路径
        String cfgPath = configPath + "/config.xml";
        String userPath = configPath + "/config-" + SystemUtils.USER_NAME + ".xml";

        NTGenUtils.output("+ 默认配置文件: %s", cfgPath);
        NTGenUtils.output("+ 用户配置文件: %s", userPath);

        // 基础配置文件
        File cfgFile = new File(cfgPath);
        if (!cfgFile.exists() || !cfgFile.isFile()) {
            NTGenUtils.output("+ 默认配置文件不存在: %s", cfgPath);
            throw new RuntimeException("默认配置文件不存在: " + cfgPath);
        }

        // 用户配置文件
        File userFile = new File(userPath);
        if (!userFile.exists() || !userFile.isFile()) {
            NTGenUtils.output("+ 用户配置文件不存在: %s", userPath);
            throw new RuntimeException("用户配置文件不存在: " + userPath);
        }

        // 1. 基础配置解析
        XMLNode cfgRoot = XMLUtils.toXMLNode(cfgFile.getAbsolutePath());

        NTGenConfig ntGenConfig = new NTGenConfig(configPath);
        ntGenConfig.setCodePath(cfgRoot.getAttributes().get(CODE_PATH_KEY));
        ntGenConfig.setCodePackage(cfgRoot.getAttributes().get(CODE_PACKAGE_KEY));
        ntGenConfig.setDtoBaseClass(cfgRoot.getAttributes().get(DTO_BASE_CLASS_KEY));

        // 数据表配置解析
        Optional<XMLNode> optTableRoot = XMLUtils.findChildByName(cfgRoot, "NTTables");
        Assert.isTrue(optTableRoot.isPresent(), "NTTables配置不存在-" + cfgRoot);

        for (XMLNode ntTable : optTableRoot.get().getChildren()) {
            NTTableConfig ntTableConfig = new NTTableConfig();
            ntTableConfig.setTableName(ntTable.getAttributes().get("name"));
            ntTableConfig.setAliasName(ntTable.getAttributes().get("alias"));
            ntTableConfig.setObjectName(ntTable.getAttributes().get("object"));

            String dtoBase = ntTable.getAttributes().get("dtoBase");
            if (StringUtils.isBlank(dtoBase)) {
                dtoBase = ntGenConfig.getDtoBaseClass();
            }
            ntTableConfig.setDtoBaseClass(dtoBase);

            // 自定义数据类型
            Optional<XMLNode> optTypeRoot = XMLUtils.findChildByName(ntTable, "NTTypes");
            if (optTypeRoot.isPresent()) {
                List<XMLNode> ntTypes = optTypeRoot.get().getChildren();
                for (XMLNode ntType : ntTypes) {
                    ntTableConfig.getUserTypes().add(ntType.getText());
                }
            }

            // 数据表字段
            Optional<XMLNode> optFieldRoot = XMLUtils.findChildByName(ntTable, "NTFields");
            Assert.isTrue(optFieldRoot.isPresent(), "NTFields配置不存在-" + ntTable);

            int indexNo = 1;
            List<XMLNode> ntFields = optFieldRoot.get().getChildren();
            for (XMLNode ntField : ntFields) {
                NTFieldConfig ntFieldConfig = new NTFieldConfig();
                ntFieldConfig.table(ntTableConfig);
                ntFieldConfig.setIndex(indexNo++);

                ntFieldConfig.setField(ntField.getAttributes().get("field"));

                String jdbcType = ntField.getAttributes().get("jdbcType");
                if (StringUtils.isBlank(jdbcType)) {
                    jdbcType = JDBCType.VARCHAR.getName();
                }
                ntFieldConfig.setJdbcType(jdbcType);


                ntFieldConfig.setJavaType(ntField.getAttributes().get("javaType"));
                ntFieldConfig.setNullable(ntField.getAttributes().get("nullable"));
                ntFieldConfig.setColumnSize(NumberUtils.toInt(ntField.getAttributes().get("maxSize")));

                ntFieldConfig.setDtoValue(StringUtils.trimToEmpty(ntField.getAttributes().get("dtoValue")));

                // 字段解析成功
                ntTableConfig.getFields().add(ntFieldConfig);
            }

            // 单表解析成功
            ntGenConfig.getTables().add(ntTableConfig);
        }

        // 2. 用户配置解析
        XMLNode userRoot = XMLUtils.toXMLNode(userFile.getAbsolutePath());

        String codePath = userRoot.getAttributes().get(CODE_PATH_KEY);
        if (StringUtils.isNotBlank(codePath)) {
            ntGenConfig.setCodePath(codePath);
        }

        // 完成返回
        return ntGenConfig;
    }

    /**
     * 填充文件参数
     */
    public static Map<String, Object> makeExecContext(NTGenConfig config, NTTableConfig table) {
        Map<String, Object> values = new ConcurrentHashMap<>();
        values.put("stamp", Long.toString(System.currentTimeMillis()));
        values.putAll(CONTEXT);

        values.put("config", config);
        values.put("table", table);

        return values;
    }

    /**
     * 获取模板渲染结果
     */
    public static String renderTemplate(NTGenConfig config, String tmptPath, Map<String, Object> values) throws Exception {
        // 模板内容
        String content = FileUtils.readFileToString(new File(config.getConfigPath() + tmptPath), CHARSET);

        // 渲染模板
        return VelocityUtils.evaluate(content, values);
    }

    /**
     * 写入生成文件
     */
    public static void writeFile(String filePath, String fileName, String content) throws Exception {
        FileUtils.write(new File(filePath, fileName), content, CHARSET);
    }

}
