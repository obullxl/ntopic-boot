/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2021 All Rights Reserved.
 */
package dalgen.querydsl;

import dalgen.core.NTGenConfig;
import dalgen.core.NTGenerate;
import dalgen.core.config.NTTableConfig;
import dalgen.core.template.NTTemplateDO;
import dalgen.querydsl.template.NTTemplateDSL;

/**
 * QueryDSL代码生成入口
 *
 * @author obullxl 2021年06月12日: 新增
 */
public class NTQueryDSLDAL extends NTGenerate {

    /* DO模板 */
    private static final String TEMPLATE_DO = "/templates/DO.vm";

    /* DSL模板 */
    private static final String TEMPLATE_DSL = "/templates/DSL.vm";

    /**
     * 生成单数据表代码
     */
    @Override
    public void onGenerateTable(NTGenConfig config, NTTableConfig table) throws Exception {
        // DO代码生成
        NTTemplateDO.onGenerate(config, table, TEMPLATE_DO);

        // DSL代码生成
        NTTemplateDSL.onGenerate(config, table, TEMPLATE_DSL);
    }

}