/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2021 All Rights Reserved.
 */
package dalgen.mybatis;

import dalgen.core.NTGenConfig;
import dalgen.core.NTGenerate;
import dalgen.core.config.NTTableConfig;
import dalgen.core.template.NTTemplateDO;

/**
 * MyBatis代码生成入口
 *
 * @author obullxl 2021年06月12日: 新增
 */
public class NTMyBatisDAL extends NTGenerate {

    /* DO模板 */
    private static final String TEMPLATE_DO = "/templates/DO.vm";

    /**
     * 生成单数据表代码
     */
    @Override
    public void onGenerateTable(NTGenConfig config, NTTableConfig table) throws Exception {
        // DO代码生成
        NTTemplateDO.onGenerate(config, table, TEMPLATE_DO);
    }

}