/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2021 All Rights Reserved.
 */
package cn.ntopic.service;

import cn.ntopic.core.exception.NTBizException;
import cn.ntopic.das.dao.UserBaseDAO;
import com.google.common.collect.Lists;
import cn.ntopic.enums.NTErrorCodeEnum;
import cn.ntopic.model.UserBaseModel;
import cn.ntopic.service.enums.UserRoleEnum;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

/**
 * NTUserX单元测试
 *
 * @author obullxl 2021年06月20日: 新增
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class NTUserServiceTest {

    @Autowired
    private NTUserService ntUserService;

    @Autowired
    private UserBaseDAO userBaseMapper;

    private static final String NAME_PREFIX = "NT-";

    @After
    public void after() {
        this.userBaseMapper.deleteByName(NAME_PREFIX + "测试");
    }

    /**
     * newUserID
     */
    @Test
    public void test_newUserID() {
        String id1 = this.ntUserService.newUserID();
        String id2 = this.ntUserService.newUserID();

        Assert.assertFalse(StringUtils.equals(id1, id2));

        Assert.assertEquals(9, StringUtils.length(id1));
        Assert.assertEquals(9, StringUtils.length(id2));

        Assert.assertTrue(StringUtils.startsWith(id1, "T"));
        Assert.assertTrue(StringUtils.startsWith(id2, "T"));
    }

    /**
     * create
     */
    @Test
    public void test_create() {
        // 用户不存在
        UserBaseModel userModel = new UserBaseModel();
        userModel.setName(NAME_PREFIX + "测试");
        userModel.setPassword("TEST");
        userModel.setRoleList(Lists.newArrayList(UserRoleEnum.USER.getCode(), UserRoleEnum.ADMIN.getCode()));

        this.ntUserService.create(userModel);

        Assert.assertTrue(StringUtils.isNotBlank(userModel.getId()));

        // 用户查询
        Optional<UserBaseModel> optUserModel = this.ntUserService.findByName(NAME_PREFIX + "测试");
        Assert.assertTrue(optUserModel.isPresent());

        // 用户存在
        try {
            this.ntUserService.create(userModel);
        } catch (NTBizException e) {
            Assert.assertEquals(NTErrorCodeEnum.USER_NAME_EXIST, e.getNtEnum());
        }
    }

}
