/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2021 All Rights Reserved.
 */
package cn.ntopic.plugin;

import com.alibaba.fastjson.JSONException;
import com.google.common.collect.Maps;
import cn.ntopic.plugin.model.NTConfigVO;
import cn.ntopic.core.utils.JSONUtils;
import cn.ntopic.core.utils.NTMapUtils;
import cn.ntopic.core.value.NTMapX;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

/**
 * @author obullxl 2021年03月20日: 新增
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class NTConfigXTest {

    private static final String TEST_PRODUCT = "NTopicBootX-TEST";

    private static final String CLEAR_DATA_SQL = "DELETE FROM nt_config WHERE product=?";

    @Autowired
    private NTConfigX ntConfigX;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Before
    public void before() {
        this.jdbcTemplate.update(CLEAR_DATA_SQL, TEST_PRODUCT);
    }

    @After
    public void after() {
        this.jdbcTemplate.update(CLEAR_DATA_SQL, TEST_PRODUCT);
    }

    @Test
    public void test_save() {
        // 1.新增
        boolean save = this.ntConfigX.save(TEST_PRODUCT, NTConfigX.DEFAULT, NTConfigX.DEFAULT, "SAVE-CONFIG-01");
        Assert.assertTrue(save);

        String config = this.ntConfigX.fetch(TEST_PRODUCT, NTConfigX.DEFAULT, NTConfigX.DEFAULT);
        Assert.assertEquals("SAVE-CONFIG-01", config);

        // 2.更新
        save = this.ntConfigX.save(TEST_PRODUCT, NTConfigX.DEFAULT, NTConfigX.DEFAULT, "SAVE-CONFIG-02");
        Assert.assertTrue(save);

        config = this.ntConfigX.fetch(TEST_PRODUCT, NTConfigX.DEFAULT, NTConfigX.DEFAULT);
        Assert.assertEquals("SAVE-CONFIG-02", config);
    }

    @Test
    public void test_update() {
        // 1.新增
        boolean update = this.ntConfigX.update(TEST_PRODUCT, NTConfigX.DEFAULT, NTConfigX.DEFAULT, "SAVE-CONFIG-11");
        Assert.assertTrue(update);

        String config = this.ntConfigX.fetch(TEST_PRODUCT, NTConfigX.DEFAULT, NTConfigX.DEFAULT);
        Assert.assertEquals("SAVE-CONFIG-11", config);

        // 2.更新
        update = this.ntConfigX.update(TEST_PRODUCT, NTConfigX.DEFAULT, NTConfigX.DEFAULT, "SAVE-CONFIG-12");
        Assert.assertTrue(update);

        config = this.ntConfigX.fetch(TEST_PRODUCT, NTConfigX.DEFAULT, NTConfigX.DEFAULT);
        Assert.assertEquals("SAVE-CONFIG-12", config);
    }

    @Test
    public void test_delete() {
        boolean delete = this.ntConfigX.delete(TEST_PRODUCT, NTConfigX.DEFAULT, NTConfigX.DEFAULT);
        Assert.assertFalse(delete);

        boolean save = this.ntConfigX.save(TEST_PRODUCT, NTConfigX.DEFAULT, NTConfigX.DEFAULT, "SAVE-CONFIG-21");
        Assert.assertTrue(save);

        delete = this.ntConfigX.delete(TEST_PRODUCT, NTConfigX.DEFAULT, NTConfigX.DEFAULT);
        Assert.assertTrue(delete);
    }

    @Test
    public void test_fetch() {
        // Ignore
    }

    @Test
    public void test_fetchMap() {
        // 1.无数据
        NTMapX map = this.ntConfigX.fetchMap(TEST_PRODUCT);
        Assert.assertTrue(NTMapUtils.isEmpty(map));

        // 2.存在数据
        Map<String, String> data = Maps.newHashMap();
        data.put("abc", "xyz");
        data.put("name", "老牛啊");

        boolean save = this.ntConfigX.save(TEST_PRODUCT, NTConfigX.DEFAULT, NTConfigX.DEFAULT, JSONUtils.format(data));
        Assert.assertTrue(save);

        map = this.ntConfigX.fetchMap(TEST_PRODUCT);
        Assert.assertEquals(2, NTMapUtils.size(map));
        Assert.assertEquals("xyz", NTMapUtils.getString(map, "abc"));
        Assert.assertEquals("老牛啊", NTMapUtils.getString(map, "name"));

        // 3.非JSON数据
        save = this.ntConfigX.save(TEST_PRODUCT, NTConfigX.DEFAULT, NTConfigX.DEFAULT, "abc=xyz");
        Assert.assertTrue(save);

        try {
            this.ntConfigX.fetchMap(TEST_PRODUCT);
        } catch (Throwable e) {
            Assert.assertTrue(e instanceof JSONException);
        }
    }

    /**
     * fetchAll
     */
    @Test
    public void test_fetchAll() {
        List<NTConfigVO> ntConfigList = this.ntConfigX.fetchAll();
        Assert.assertNotNull(ntConfigList);
    }

}
