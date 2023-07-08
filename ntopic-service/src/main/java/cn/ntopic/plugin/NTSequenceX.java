/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2021 All Rights Reserved.
 */
package cn.ntopic.plugin;

import cn.ntopic.LogConstants;
import cn.ntopic.core.builder.ToString;
import cn.ntopic.core.exception.NTSequenceException;
import com.google.common.collect.Maps;
import cn.ntopic.core.value.NTTuple;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 序列服务插件
 *
 * @author obullxl 2021年03月20日: 新增
 */
public class NTSequenceX {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogConstants.PLUGIN);

    /**
     * 默认序列名称
     */
    public static final String DEFAULT_SEQ_NAME = "NTopic-BootX";

    /**
     * 序列最大重试获取次数
     */
    private static final int MAX_RETRY_TIMES = 10;

    /**
     * 序列递增步长
     */
    private static final int SEQ_INC_STEP = 100;

    /**
     * 序列长度
     */
    public static final int MAX_SEQ_LENGTH = 8;

    /**
     * 序列最大值
     */
    private static final long SEQ_MAX_VALUE = 99999999L;

    /**
     * 字段名-序列版本
     */
    private static final String FIELD_VERSION = "version";

    /**
     * 字段名-序列增长步长
     */
    private static final String FIELD_STEP = "step";

    /**
     * 字段名-序列当前值
     */
    private static final String FIELD_VALUE = "value";

    /**
     * 字段名-序列最小值
     */
    private static final String FIELD_MIN_VALUE = "min_value";

    /**
     * 字段名-序列最大值
     */
    private static final String FIELD_MAX_VALUE = "max_value";

    /**
     * 新增序列SQL
     */
    private static final String INSERT_SQL = "INSERT INTO nt_sequence (name,version,step,value,min_value,max_value) VALUES (?,?,?,?,?,?);";

    /**
     * 查询序列SQL
     */
    private static final String SELECT_SQL = "SELECT * FROM nt_sequence WHERE name=?";

    /**
     * 更新序列SQL
     */
    private static final String UPDATE_VALUE_SQL = "UPDATE nt_sequence SET version=version+1,value=? WHERE name=? AND version=?";

    /**
     * JDBC模板
     */
    private final JdbcTemplate jdbcTemplate;

    /**
     * 序列配置项
     */
    private static final ConcurrentMap<String, SequenceItem> SEQ_ITEM_MAP = Maps.newConcurrentMap();

    /**
     * 初始化
     */
    public NTSequenceX(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;

        // 初始化默认配置
        this.init(DEFAULT_SEQ_NAME, 1L, SEQ_MAX_VALUE, SEQ_INC_STEP);
    }

    /**
     * 初始化序列记录
     *
     * @param seqName 序列名称
     */
    public void init(String seqName, long minValue, long maxValue, int incStep) {
        Assert.isTrue(StringUtils.isNotBlank(seqName), "NTSequenceX:初始化-序列名为空");
        Assert.isTrue(minValue > 0L, "NTSequenceX:初始化-最小值非法");
        Assert.isTrue(maxValue > minValue, "NTSequenceX:初始化-最大值非法");
        Assert.isTrue(incStep > 1, "NTSequenceX:初始化-递增步长非法");

        LOGGER.info("NTSequenceX:初始化序列[{}]-Min[{}]-Max[{}]-Step[{}].", seqName, minValue, maxValue, incStep);

        try {
            Map<String, Object> record = this.jdbcTemplate.queryForMap(SELECT_SQL, seqName);
            Assert.isTrue(MapUtils.isNotEmpty(record), "NTSequenceX:序列不存在");
        } catch (EmptyResultDataAccessException e) {
            try {
                this.jdbcTemplate.update(INSERT_SQL, seqName, 1L, incStep, minValue, minValue, maxValue);
            } catch (DataIntegrityViolationException ex) {
                LOGGER.warn("NTSequenceX:序列已经存在[{}].", seqName);
            }
        }
    }

    /**
     * 获取默认序列值，前缀补`0`
     */
    public String nextValue() {
        return this.nextValue(DEFAULT_SEQ_NAME);
    }

    /**
     * 获取指定序列值，前缀补`0`
     */
    public String nextValue(String seqName) {
        return StringUtils.leftPad(Long.toString(this.next(seqName)), MAX_SEQ_LENGTH, "0");
    }

    /**
     * 获取1个默认序列
     *
     * @see #DEFAULT_SEQ_NAME 默认序列名称
     */
    public long next() {
        return this.next(DEFAULT_SEQ_NAME);
    }

    /**
     * 获取1个序列
     *
     * @param seqName 序列名称，不能为空
     */
    public long next(String seqName) {
        Assert.isTrue(StringUtils.isNotBlank(seqName), "NTSequenceX:序列名为空");

        synchronized (SEQ_ITEM_MAP) {
            SequenceItem item = SEQ_ITEM_MAP.get(seqName);
            if (item == null) {
                LOGGER.debug("NTSequenceX:序列缓存缺失[{}].", seqName);

                // 新建序列区间
                NTTuple<Long, Long> range = this.makeSequenceRange(seqName);
                item = new SequenceItem(range.getOne(), range.getTwo());
                SEQ_ITEM_MAP.put(seqName, item);

                LOGGER.debug("NTSequenceX:更新序列缓存[{}]-{}.", seqName, item);
            }

            // 获取新增
            long value = item.nextValue();
            if (value <= item.atomicMax.get()) {
                return value;
            }

            // 刷新序列区间
            LOGGER.debug("NTSequenceX:重建序列缓存[{}]-原序列:{}.", seqName, item);

            NTTuple<Long, Long> temp = this.makeSequenceRange(seqName);
            LOGGER.debug("NTSequenceX:重新计算序列[{}]-{}.", seqName, temp);

            if (item.reset(item.atomicMin.get(), item.atomicMax.get(), temp)) {
                LOGGER.debug("NTSequenceX:重建序列缓存[{}]-新序列:{}.", seqName, item);

                return item.nextValue();
            }
        }

        // 3.尝试获取序列失败
        throw new NTSequenceException("NTSequenceX:获取序列值失败[" + seqName + "]");
    }

    /**
     * 构建序列
     */
    private NTTuple<Long, Long> makeSequenceRange(final String seqName) {
        for (int i = 1; i <= MAX_RETRY_TIMES; i++) {
            // 1.锁记录
            Map<String, Object> record = this.jdbcTemplate.queryForMap(SELECT_SQL, seqName);
            LOGGER.info("NTSequenceX:No.{}-当前序列[{}]-{}.", i, seqName, record);

            // 2.计算更新记录
            long version = MapUtils.getLongValue(record, FIELD_VERSION);
            long step = MapUtils.getLongValue(record, FIELD_STEP);
            long value = MapUtils.getLongValue(record, FIELD_VALUE);
            long minValue = MapUtils.getLongValue(record, FIELD_MIN_VALUE);
            long maxValue = MapUtils.getLongValue(record, FIELD_MAX_VALUE);

            Assert.isTrue(step >= 1L, "NTSequenceX:序列步长非法[" + seqName + "]");
            Assert.isTrue(minValue >= 1L, "NTSequenceX:序列最小值非法[" + seqName + "]");
            Assert.isTrue(maxValue > minValue, "NTSequenceX:序列最小最大值非法[" + seqName + "]");
            Assert.isTrue(maxValue > (minValue + step), "NTSequenceX:序列最小最大值非法[" + seqName + "]");

            // 计算
            long newValue = value + step;
            long min = value;
            if (newValue <= 0L || newValue > maxValue) {
                newValue = minValue + step;
                min = minValue;
            }

            // 更新
            boolean update = (this.jdbcTemplate.update(UPDATE_VALUE_SQL, newValue, seqName, version) >= 1);
            if (!update) {
                LOGGER.warn("NTSequenceX:No.{}-更新序列失败[{}].", i, seqName);
                continue;
            }

            LOGGER.info("NTSequenceX:No.{}-序列增长[{}]-[{}].", i, seqName, newValue);

            // 返回序列区间
            return NTTuple.of(min + 1, newValue);
        }

        LOGGER.warn("NTSequenceX:获取序列区间失败[{}].", seqName);
        throw new NTSequenceException("NTSequenceX:获取序列区间失败[" + seqName + "]");
    }

    /**
     * 当前序列配置
     */
    private static class SequenceItem extends ToString {

        /**
         * 最小值
         */
        private final AtomicLong atomicMin;

        /**
         * 最大值
         */
        private final AtomicLong atomicMax;

        /**
         * 当前值
         */
        private final AtomicLong atomicValue;

        /**
         * 初始化
         */
        public SequenceItem(long minValue, long maxValue) {
            Assert.isTrue(minValue > 0L, "NTSequenceX#SequenceItem:最小值非法");
            Assert.isTrue(maxValue > minValue, "NTSequenceX#SequenceItem:最大值非法");

            this.atomicMin = new AtomicLong(minValue);
            this.atomicMax = new AtomicLong(maxValue);
            this.atomicValue = new AtomicLong(minValue - 1L);
        }

        /**
         * 检测是否有下一个值
         */
        public long nextValue() {
            return this.atomicValue.incrementAndGet();
        }

        /**
         * 重置序列配置
         *
         * @param minValue  原最小值
         * @param maxValue  原最大值
         * @param newMinMax 重置最小和最大值
         */
        public boolean reset(long minValue, long maxValue, NTTuple<Long, Long> newMinMax) {
            Assert.isTrue(minValue > 0L, "NTSequenceX#SequenceItem:最小值非法");
            Assert.isTrue(maxValue > minValue, "NTSequenceX#SequenceItem:最大值非法");
            Assert.isTrue(newMinMax.getOne() > 0L, "NTSequenceX#SequenceItem:新最小值非法");
            Assert.isTrue(newMinMax.getTwo() > newMinMax.getOne(), "NTSequenceX#SequenceItem:新最大值非法");

            if (this.atomicMin.compareAndSet(minValue, newMinMax.getOne()) &&
                    this.atomicMax.compareAndSet(maxValue, newMinMax.getTwo())) {
                this.atomicValue.set(newMinMax.getOne() - 1L);
                return true;
            }

            // 原参数不匹配
            return false;
        }
    }

}
