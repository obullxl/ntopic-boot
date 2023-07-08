/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2021 All Rights Reserved.
 */
package cn.ntopic.plugin;

import cn.ntopic.LogConstants;
import cn.ntopic.plugin.model.NTConfigVO;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import cn.ntopic.core.utils.NTMapUtils;
import cn.ntopic.core.value.NTMapX;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * 配置服务插件
 * <p>
 * 配置由三级组成：1级为产品，二级为模块，三级为功能，三级唯一确定一个配置！
 *
 * @author obullxl 2021年03月20日: 新增
 */
public final class NTConfigX {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogConstants.PLUGIN);

    /**
     * 默认值
     */
    public static final String DEFAULT = "DEFAULT";

    /**
     * `config`数据表字段
     */
    private static final String CONFIG_FIELD = "config";

    /**
     * 配置新增SQL
     */
    private static final String INSERT_SQL = "INSERT INTO nt_config (product,module,function,config,create_time,modify_time) VALUES (?,?,?,?,NOW(),NOW());";

    /**
     * 配置更新SQL
     */
    private static final String UPDATE_ONE = "UPDATE nt_config SET modify_time=NOW(),config=? WHERE product=? AND `module`=? AND `function`=?";

    /**
     * 配置删除SQL
     */
    private static final String DELETE_ONE = "DELETE FROM nt_config WHERE product=? AND `module`=? AND `function`=?";

    /**
     * 查询配置SQL
     */
    private static final String SELECT_ONE = "SELECT config FROM nt_config WHERE product=? AND `module`=? AND `function`=?";

    /**
     * 查询所有SQL
     */
    private static final String SELECT_ALL = "SELECT * FROM nt_config";

    /**
     * Key链接符
     */
    private static final String KEY_SEPARATOR = "#";

    /**
     * JDBC模板
     */
    private final JdbcTemplate jdbcTemplate;

    /**
     * 本地缓存
     */
    private final LoadingCache<String, String> cache;

    /**
     * 初始化
     */
    public NTConfigX(JdbcTemplate jdbcTemplate) {
        this(jdbcTemplate, 1000, 30);
    }

    /**
     * 初始化
     */
    public NTConfigX(JdbcTemplate jdbcTemplate, int maximumSize, int expireSeconds) {
        this.jdbcTemplate = jdbcTemplate;

        this.cache = CacheBuilder.newBuilder()
                .maximumSize(maximumSize)
                .expireAfterWrite(expireSeconds, TimeUnit.SECONDS)
                .build(new CacheLoader<String, String>() {
                    @Override
                    public String load(String key) {
                        String[] keyParts = StringUtils.split(key, KEY_SEPARATOR);
                        if (keyParts == null || keyParts.length != 3) {
                            LOGGER.warn("NTConfigX缓存加载-Key无效[{}].", key);
                            return StringUtils.EMPTY;
                        }

                        // DB加载
                        return NTConfigX.this.load(keyParts[0], keyParts[1], keyParts[2]);
                    }
                });
    }

    /**
     * 参数检测
     */
    private void check(String product, String module, String function) {
        Assert.isTrue(StringUtils.isNotBlank(product), "NTConfigX:产品为空");
        Assert.isTrue(StringUtils.isNotBlank(module), "NTConfigX:模块为空");
        Assert.isTrue(StringUtils.isNotBlank(function), "NTConfigX:功能为空");
    }

    /**
     * 新增或者更新配置
     */
    public boolean save(String product, String module, String function, String config) {
        // 参数检测
        this.check(product, module, function);

        // 缓存失效
        this.cache.invalidate(this.formatKey(product, module, function));

        try {
            // 插入数据
            return (this.jdbcTemplate.update(INSERT_SQL, product, module, function, config) >= 1);
        } catch (DataIntegrityViolationException e) {
            // 配置已经存在，更新配置
            return (this.jdbcTemplate.update(UPDATE_ONE, config, product, module, function) >= 1);
        }
    }

    /**
     * 更新或者新增配置
     */
    public boolean update(String product, String module, String function, String config) {
        // 参数检测
        this.check(product, module, function);

        // 缓存失效
        this.cache.invalidate(this.formatKey(product, module, function));

        // 更新数据
        if (this.jdbcTemplate.update(UPDATE_ONE, config, product, module, function) >= 1) {
            return true;
        }

        // 插入数据
        boolean insert = (this.jdbcTemplate.update(INSERT_SQL, product, module, function, config) >= 1);
        if (!insert) {
            LOGGER.warn("NTConfigX:更新失败-[{}]-[{}]-[{}]-[{}].", product, module, function, config);
        }

        return insert;
    }

    /**
     * 删除配置
     */
    public boolean delete(String product, String module, String function) {
        LOGGER.warn("NTConfigX:删除配置-[{}]-[{}]-[{}].", product, module, function);

        // 参数检测
        this.check(product, module, function);

        // 缓存失效
        this.cache.invalidate(this.formatKey(product, module, function));

        return (this.jdbcTemplate.update(DELETE_ONE, product, module, function) >= 1);
    }

    /**
     * 缓存加载
     */
    private String load(String product, String module, String function) {
        try {
            // 参数检测
            this.check(product, module, function);

            Map<String, Object> record = this.jdbcTemplate.queryForMap(SELECT_ONE, product, module, function);
            return StringUtils.trimToEmpty(MapUtils.getString(record, CONFIG_FIELD));
        } catch (EmptyResultDataAccessException e) {
            LOGGER.warn("NTConfigX:配置记录不存在[{},{},{}].", product, module, function);
        } catch (Throwable e) {
            LOGGER.warn("NTConfigX:缓存加载异常[{},{},{}].", product, module, function, e);
        }

        return StringUtils.EMPTY;
    }

    /**
     * 缓存Key组装
     */
    private String formatKey(String product, String module, String function) {
        return product + KEY_SEPARATOR + module + KEY_SEPARATOR + function;
    }

    /**
     * 缓存获取配置
     */
    public String fetch(String product, String module, String function) {
        // 参数检测
        this.check(product, module, function);

        try {
            return this.cache.get(this.formatKey(product, module, function));
        } catch (Throwable e) {
            LOGGER.warn("NTConfigX:缓存获取异常[{},{},{}].", product, module, function, e);
            throw new RuntimeException("NTConfigX:缓存获取异常.", e);
        }
    }

    /**
     * 从DB获取配置对象
     */
    public String fetchDB(String product, String module, String function) {
        // 参数检测
        this.check(product, module, function);

        try {
            Map<String, Object> record = this.jdbcTemplate.queryForMap(SELECT_ONE, product, module, function);
            return MapUtils.getString(record, CONFIG_FIELD);
        } catch (EmptyResultDataAccessException e) {
            LOGGER.warn("NTConfigX:配置记录不存在[{},{},{}].", product, module, function);
            return StringUtils.EMPTY;
        }
    }

    /**
     * 获取配置对象
     */
    public NTMapX fetchMap(String product) {
        return this.fetchMap(product, DEFAULT);
    }

    /**
     * 获取配置对象
     */
    public NTMapX fetchMap(String product, String module) {
        return this.fetchMap(product, module, DEFAULT);
    }

    /**
     * 获取配置对象
     */
    public NTMapX fetchMap(String product, String module, String function) {
        return NTMapX.with(this.fetch(product, module, function));
    }

    /**
     * 获取所有配置
     */
    public List<NTConfigVO> fetchAll() {
        return this.convert(this.jdbcTemplate.queryForList(SELECT_ALL));
    }

    /**
     * 对象列表转换
     */
    private List<NTConfigVO> convert(List<Map<String, Object>> recordList) {
        List<NTConfigVO> configList = new ArrayList<>();
        for (Map<String, Object> record : recordList) {
            this.convert(record).ifPresent(configList::add);
        }

        return configList;
    }

    /**
     * 对象转换
     */
    private Optional<NTConfigVO> convert(Map<String, Object> record) {
        if (MapUtils.isEmpty(record)) {
            return Optional.empty();
        }

        NTConfigVO ntConfigVO = new NTConfigVO();
        ntConfigVO.setProduct(MapUtils.getString(record, "product"));
        ntConfigVO.setModule(MapUtils.getString(record, "module"));
        ntConfigVO.setFunction(MapUtils.getString(record, "function"));
        ntConfigVO.setConfig(MapUtils.getString(record, CONFIG_FIELD));
        ntConfigVO.setCreateTime(NTMapUtils.getDate(record, "create_time"));
        ntConfigVO.setModifyTime(NTMapUtils.getDate(record, "modify_time"));

        return Optional.of(ntConfigVO);
    }

}
