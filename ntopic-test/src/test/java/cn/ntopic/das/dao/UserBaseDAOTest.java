/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2021 All Rights Reserved.
 */
package cn.ntopic.das.dao;

import cn.ntopic.NTBootApplication;
import cn.ntopic.das.model.UserBaseDO;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

/**
 * @author obullxl 2021年10月17日: 新增
 */
@SpringBootTest(classes = {NTBootApplication.class})
@RunWith(SpringRunner.class)
public class UserBaseDAOTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    @Qualifier("userBaseDAO")
    private UserBaseDAO userBaseDAO;

    /**
     * CRUD简单测试
     */
    @Test
    public void test_insert_select_update_delete() {
        final String id = "ID-" + RandomUtils.nextLong();
        final String name = "NAME-" + RandomUtils.nextLong();

        // 1. 清理数据
        this.userBaseDAO.deleteById(id);
        this.userBaseDAO.deleteByName(name);

        // 请求数据 - 验证
        UserBaseDO userBaseDO = this.userBaseDAO.selectById(id);
        Assert.assertNull(userBaseDO);

        userBaseDO = this.userBaseDAO.selectByName(name);
        Assert.assertNull(userBaseDO);

        try {
            // 2. 新增数据
            UserBaseDO newUserDO = new UserBaseDO();
            newUserDO.setId(id);
            newUserDO.setName(name);
            newUserDO.setCreateTime(new Date());
            newUserDO.setModifyTime(new Date());
            this.userBaseDAO.insert(newUserDO);

            // 3. 数据查询 - 新增验证
            UserBaseDO checkUserDO = this.userBaseDAO.selectById(id);
            Assert.assertNotNull(checkUserDO);
            Assert.assertEquals(name, checkUserDO.getName());
            Assert.assertNotNull(checkUserDO.getCreateTime());
            Assert.assertNotNull(checkUserDO.getModifyTime());
            Assert.assertTrue(StringUtils.isBlank(checkUserDO.getRoleList()));

            // 4. 更新数据
            int update = this.userBaseDAO.updateRoleList(id, new Date(), "ADMIN,DATA");
            Assert.assertEquals(1, update);

            // 更新数据 - 验证
            checkUserDO = this.userBaseDAO.selectById(id);
            Assert.assertNotNull(checkUserDO);
            Assert.assertEquals("ADMIN,DATA", checkUserDO.getRoleList());

            // 5. 数据删除
            int delete = this.userBaseDAO.deleteById(id);
            Assert.assertEquals(1, delete);

            delete = this.userBaseDAO.deleteByName(name);
            Assert.assertEquals(0, delete);
        } finally {
            // 清理数据
            this.userBaseDAO.deleteById(id);
        }
    }
}
