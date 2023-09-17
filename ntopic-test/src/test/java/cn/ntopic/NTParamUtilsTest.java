/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2023 All Rights Reserved.
 */
package cn.ntopic;

import cn.ntopic.das.model.NTParamDO;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * @author obullxl 2023年09月10日: 新增
 */
public class NTParamUtilsTest extends NTAbstractTest {

    @Test
    public void test_query() {
        List<NTParamDO> paramList = NTParamUtils.findList("CONFIG", "USER");
        Assert.assertNotNull(paramList);
    }
}
