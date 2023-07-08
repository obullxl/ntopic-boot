/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2021 All Rights Reserved.
 */
package cn.ntopic.plugin;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author obullxl 2021年03月20日: 新增
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class NTSequenceXTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(NTSequenceXTest.class);

    @Autowired
    private NTSequenceX ntSequenceX;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void test_init() {
        this.ntSequenceX.init("NTopic-UserBase", 1L, 99999999L, 10);

        List<Map<String, Object>> seqList = this.jdbcTemplate.queryForList("SELECT * FROM nt_sequence");
        Assert.assertTrue(seqList.size() >= 2);

        Set<String> seqNameSet = seqList.stream().map(seq -> seq.get("name")).map(Objects::toString).collect(Collectors.toSet());
        Assert.assertTrue(seqNameSet.contains(NTSequenceX.DEFAULT_SEQ_NAME));
        Assert.assertTrue(seqNameSet.contains("NTopic-UserBase"));

        System.out.println("NTSequenceX:Test初始化序列列表：" + seqList);
    }

    @Test
    public void test_next() {
        long value1 = this.ntSequenceX.next();
        LOGGER.warn("NTSequenceX::序列值[{}].", value1);

        Assert.assertTrue(value1 > 0L);

        long value2 = this.ntSequenceX.next();
        LOGGER.warn("NTSequenceX::序列值[{}].", value2);

        Assert.assertTrue(value2 > value1);

        for (int i = 1; i <= 1377; i++) {
            long value3 = this.ntSequenceX.next();
            LOGGER.warn("NTSequenceX::序列值[{}].", value3);

            Assert.assertTrue(value3 > value2);

            value2 = value3;
        }
    }

}
