/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2021 All Rights Reserved.
 */
package dalgen;

import com.google.common.collect.Lists;
import dalgen.querydsl.NTQueryDSLDAL;

import java.util.List;

/**
 * QueryDSL代码生成测试
 *
 * @author obullxl 2021年06月12日: 新增
 */
public class NTQueryDSLMain {

    /**
     * 代码生成
     */
    public static void main(String[] args) throws Exception {
        List<String> execArgs = Lists.newArrayList(args);
        execArgs.add("-DconfigPath=/Users/obullxl/CodeSpace/ntopic-boot/dalgen/QueryDSL");

        new NTQueryDSLDAL().onGenerate(execArgs);
    }

}
