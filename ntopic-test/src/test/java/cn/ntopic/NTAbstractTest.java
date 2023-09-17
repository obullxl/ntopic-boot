/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2022 All Rights Reserved.
 */
package cn.ntopic;

import cn.ntopic.bootstrap.NTApplicationContextInitializer;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author obullxl 2022年01月09日: 新增
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(initializers = {NTApplicationContextInitializer.class})
public abstract class NTAbstractTest extends AbstractJUnit4SpringContextTests {
}
