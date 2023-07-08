/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2021 All Rights Reserved.
 */
package dalgen.core;

import com.google.common.base.Joiner;
import dalgen.core.config.NTFieldConfig;
import dalgen.core.config.NTTableConfig;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 代码生成基类
 *
 * @author obullxl 2021年06月12日: 新增
 */
public abstract class NTGenerate {

    /**
     * 单个表代码生成
     */
    public abstract void onGenerateTable(NTGenConfig config, NTTableConfig table) throws Exception;

    /**
     * 开始生成代码
     */
    public final void onGenerate(List<String> args) throws Exception {
        // 默认参数
        NTGenUtils.CONTEXT.put("year", new SimpleDateFormat("yyyy").format(new Date()));
        NTGenUtils.CONTEXT.put("dateYMD", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

        // 执行参数
        Map<String, String> execArgs = new ConcurrentHashMap<>();
        execArgs.putAll(NTGenUtils.CONTEXT);
        execArgs.putAll(NTGenUtils.makeExecArgsMap(args));

        NTGenUtils.output("+ 代码生成器参数：");
        execArgs.forEach((key, value) -> NTGenUtils.output("| %s=%s", key, value));

        // 配置参数
        NTGenConfig config = NTGenUtils.makeConfig(execArgs);
        config.checkExecute();

        String codePath = config.getCodePath();
        String codePackage = config.getCodePackage();

        NTGenUtils.output("-------------------------------------");
        NTGenUtils.output("| codePath=%s", codePath);
        NTGenUtils.output("| codePackage=%s", codePackage);

        // 生成提示
        int tableNo = 1;
        StringBuilder txt = new StringBuilder(256);
        for (NTTableConfig table : config.getTables()) {
            if (tableNo > 1) {
                txt.append("\n");
            }

            txt.append(tableNo++).append(". ");
            txt.append(table.getTableName());
            txt.append("[").append(table.getAliasName()).append("]");
            txt.append(" -> ").append(table.getObjectName());

            if (table.getFields().size() > 0) {
                txt.append(" (");

                List<String> fields = table.getFields().stream()
                        .map(NTFieldConfig::getField).collect(Collectors.toList());
                txt.append(Joiner.on(",").join(fields));

                txt.append(")");
            }
        }

        NTGenUtils.output("\n+ 自动生成数据表[%s]个:", config.getTables().size());
        NTGenUtils.output(txt.toString());

        // 代码生成
        NTGenUtils.CONTEXT.put(NTGenUtils.CODE_PACKAGE_KEY, codePackage);

        for (NTTableConfig table : config.getTables()) {
            NTGenUtils.output("-------------------------------------");
            NTGenUtils.output("+ 开始生成数据表代码[%s].", table.getTableName());

            // 生成代码文件
            FileUtils.forceMkdir(new File(codePath));
            FileUtils.forceMkdir(new File(codePath, StringUtils.replace(codePackage, ".", "/")));

            // DO代码生成
            this.onGenerateTable(config, table);

            NTGenUtils.output("+ 数据表代码生成完成[%s].", table.getTableName());
            NTGenUtils.output("-------------------------------------");
        }
    }

}