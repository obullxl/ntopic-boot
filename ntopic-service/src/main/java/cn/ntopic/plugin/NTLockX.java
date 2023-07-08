/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2021 All Rights Reserved.
 */
package cn.ntopic.plugin;

import cn.ntopic.core.utils.HostUtils;
import cn.ntopic.plugin.model.NTLockResult;
import cn.ntopic.core.utils.DateUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.Map;

/**
 * 分布式锁插件
 *
 * @author obullxl 2021年03月27日: 新增
 */
public class NTLockX {
    private static final Logger LOGGER = LoggerFactory.getLogger(NTLockX.class);

    /**
     * 字段名-锁版本
     */
    private static final String FIELD_VERSION = "version";

    /**
     * 字段名-锁占有服务器
     */
    private static final String FIELD_LOCK_HOST = "lock_host";

    /**
     * 字段名-锁超时时间
     */
    private static final String FIELD_EXPIRE_TIME = "expire_time";

    /**
     * 分布式锁新增SQL
     */
    private static final String INSERT_SQL = "INSERT INTO nt_lock (lock_id,group,version,lock_host,expire_time) VALUES (?,?,1,?,?);";

    /**
     * 分布式锁更新SQL
     */
    private static final String UPDATE_SQL = "UPDATE nt_lock SET version=version+1,lock_host=?,expire_time=? WHERE lock_id=? AND version=?";

    /**
     * 分布式锁删除SQL
     */
    private static final String DELETE_SQL = "DELETE FROM nt_lock WHERE lock_id=? AND version=?";

    /**
     * 分布式锁释放SQL
     */
    private static final String SELECT_SQL = "SELECT * FROM nt_lock WHERE lock_id=?";

    /**
     * 分布式锁清理SQL
     */
    private static final String CLEAN_SQL = "DELETE FROM nt_lock WHERE expire_time<=?";

    /**
     * JDBC模板
     */
    private final JdbcTemplate jdbcTemplate;

    /**
     * 初始化
     */
    public NTLockX(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * 清理超时锁
     */
    public void cleanExpireLocks() {
        try {
            this.jdbcTemplate.update(CLEAN_SQL, DateUtils.formatFLS(new Date()));
        } catch (Throwable e) {
            LOGGER.error("NTLockX:清理超时锁异常.", e);
        }
    }

    /**
     * 尝试锁定
     *
     * @param lockId        非空，锁ID
     * @param timeoutMillis 锁时长，单位毫秒
     */
    public NTLockResult tryLock(String lockId, long timeoutMillis) {
        Assert.isTrue(StringUtils.isNotBlank(lockId), "NTLockX:锁ID为空");
        Assert.isTrue(timeoutMillis > 0L, "NTLockX:锁时长参数非法");

        // 服务器
        String lockHost = HostUtils.fetchHostName();
        String lockGroup = NTLockResult.DEFAULT;

        // 锁时间
        Date nowTime = new Date();
        Date expireTime = new Date(nowTime.getTime() + timeoutMillis);
        String fmtExpireTime = DateUtils.formatFLS(expireTime);

        NTLockResult ntLockResult = new NTLockResult(lockId);
        ntLockResult.setVersion(-1L);
        ntLockResult.setExpireTime(fmtExpireTime);
        ntLockResult.setLockHost(lockHost);
        try {
            // 插入锁
            boolean insert = (this.jdbcTemplate.update(INSERT_SQL, lockId, lockGroup, lockHost, fmtExpireTime) >= 1);

            ntLockResult.setSuccess(insert);
            ntLockResult.setVersion(insert ? 1L : -1L);
            return ntLockResult;
        } catch (DataIntegrityViolationException e) {
            // 锁冲突，超时检测
            try {
                Map<String, Object> ntRecord = this.jdbcTemplate.queryForMap(SELECT_SQL, lockId);

                long existVersion = MapUtils.getLongValue(ntRecord, FIELD_VERSION);
                Date existExpireTime = DateUtils.parseFLS(MapUtils.getString(ntRecord, FIELD_EXPIRE_TIME));
                if (existExpireTime != null && existExpireTime.after(nowTime)) {
                    // 锁已经被抢占
                    LOGGER.info("NTLockX:锁被抢占-{}.", ntRecord);

                    ntLockResult.setSuccess(false);
                    ntLockResult.setVersion(existVersion);
                    ntLockResult.setLockHost(MapUtils.getString(ntRecord, FIELD_LOCK_HOST));
                    ntLockResult.setExpireTime(MapUtils.getString(ntRecord, FIELD_EXPIRE_TIME));
                    ntLockResult.setMessage("NTLockX:锁被抢占");
                    return ntLockResult;
                }

                // 锁已经尝试，尝试抢占
                boolean update = (this.jdbcTemplate.update(UPDATE_SQL, lockHost, fmtExpireTime, lockId, existVersion) >= 1);
                ntLockResult.setSuccess(update);
                ntLockResult.setVersion(update ? existVersion + 1L : -1L);
                return ntLockResult;
            } catch (Throwable ex) {
                LOGGER.error("NTLockX:尝试锁定异常[{}].", lockId, ex);

                ntLockResult.setSuccess(false);
                ntLockResult.setVersion(-1L);
                ntLockResult.setMessage("NTLockX:锁定异常-" + ex.getMessage());
                return ntLockResult;
            }
        }
    }

    /**
     * 释放锁
     *
     * @param ntLockResult 原锁定返回结果
     */
    public boolean releaseLock(NTLockResult ntLockResult) {
        Assert.notNull(ntLockResult, "NTLockX:锁对象为NULL");

        try {
            return (this.jdbcTemplate.update(DELETE_SQL, ntLockResult.getLockId(), ntLockResult.getVersion()) >= 1);
        } catch (Throwable e) {
            LOGGER.error("NTLockX:锁缩放异常-{}.", ntLockResult, e);
            return false;
        }
    }

    /**
     * 延长锁定
     *
     * @param ntLockResult        已锁定结果
     * @param extendTimeoutMillis 延长锁定时长，单位毫秒
     */
    public void extendTimeout(NTLockResult ntLockResult, long extendTimeoutMillis) {
        Assert.notNull(ntLockResult, "NTLockX:锁对象为NULL");
        Assert.isTrue(StringUtils.isNotBlank(ntLockResult.getLockId()), "NTLockX:锁ID为空");
        Assert.isTrue(extendTimeoutMillis > 0L, "NTLockX:锁延长时长参数非法");

        try {
            Date rawExpire = DateUtils.parseFLS(ntLockResult.getExpireTime());
            if (rawExpire == null) {
                LOGGER.warn("NTLockX:锁延长超时时间为空-{}.", ntLockResult);

                ntLockResult.setSuccess(false);
                ntLockResult.setMessage("NTLockX:锁延长超时时间为空");
                return;
            }

            // 更新超时时间
            Date newExpire = new Date(rawExpire.getTime() + extendTimeoutMillis);
            if (!newExpire.after(new Date())) {
                LOGGER.warn("NTLockX:锁延长超时已过期-{}.", ntLockResult);

                ntLockResult.setSuccess(false);
                ntLockResult.setMessage("NTLockX:锁延长超时已过期");
                return;
            }

            String lockHost = HostUtils.fetchHostName();
            long rawLockVersion = ntLockResult.getVersion();
            String fmtExpireTime = DateUtils.formatFLS(newExpire);
            if (this.jdbcTemplate.update(UPDATE_SQL, lockHost, fmtExpireTime, ntLockResult.getLockId(), rawLockVersion) >= 1) {
                ntLockResult.setSuccess(true);
                ntLockResult.setLockHost(lockHost);
                ntLockResult.setVersion(rawLockVersion);
                ntLockResult.setExpireTime(fmtExpireTime);
            } else {
                ntLockResult.setSuccess(false);
                ntLockResult.setMessage("NTLockX:锁延长超时失败");
            }
        } catch (Throwable e) {
            LOGGER.error("NTLockX:锁延长超时异常-{}.", ntLockResult, e);

            ntLockResult.setSuccess(false);
            ntLockResult.setMessage("NTLockX:锁延长超时异常-" + e.getMessage());
        }
    }

}
