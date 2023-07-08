/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2021 All Rights Reserved.
 */
package dalgen.querydsl.template;

import dalgen.core.NTGenConfig;
import dalgen.core.NTGenUtils;
import dalgen.core.config.NTTableConfig;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * DSL代码生成模板
 *
 * @author obullxl 2021年06月12日: 新增
 */
public final class NTTemplateDSL {

    /**
     * 生成代码
     */
    public static void onGenerate(NTGenConfig config, NTTableConfig table, String template) throws Exception {
        // 路径信息
        String codePath = StringUtils.replace(config.getCodePackage(), ".", "/");
        String objectPath = config.getCodePath() + "/" + codePath + "/table";

        // 文件生成
        Map<String, Object> values = NTGenUtils.makeExecContext(config, table);

        String content = NTGenUtils.renderTemplate(config, template, values);
        NTGenUtils.writeFile(objectPath, table.getObjectName() + "DSL.java", content);
    }

}