/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2021 All Rights Reserved.
 */
package cn.ntopic.plugin.model;

import cn.ntopic.core.builder.ToString;

/**
 * 分布式锁结果
 *
 * @author obullxl 2021年03月27日: 新增
 */
public class NTLockResult extends ToString {

    /**
     * 锁默认分组
     */
    public static final String DEFAULT = "DEFAULT";

    /**
     * 锁ID
     */
    private final String lockId;

    /**
     * 锁分组
     */
    private String lockGroup = DEFAULT;

    /**
     * 锁定结果
     */
    private boolean success;

    /**
     * 锁版本
     */
    private long version;

    /**
     * 锁定服务器
     */
    private String lockHost;

    /**
     * 超时时间，`yyyy-MM-dd HH:mm:ss.SSS`格式
     */
    private String expireTime;

    /**
     * 锁定描述
     */
    private String message;

    /**
     * CTOR
     */
    public NTLockResult(String lockId) {
        this.lockId = lockId;
    }

    /**
     * 复制锁另外对象，覆盖本对象属性
     *
     * @param other 待复制对象
     * @return 本锁对象
     */
    public NTLockResult from(NTLockResult other) {
        if (other == null) {
            return this;
        }

        this.setLockGroup(other.getLockGroup());
        this.setSuccess(other.isSuccess());
        this.setVersion(other.getVersion());
        this.setLockHost(other.getLockHost());
        this.setExpireTime(other.getExpireTime());

        return this;
    }

    // ~~~~~~~~~~~~~~~~ getters and setters ~~~~~~~~~~~~~~~~ //

    public String getLockId() {
        return lockId;
    }

    public String getLockGroup() {
        return lockGroup;
    }

    public void setLockGroup(String lockGroup) {
        this.lockGroup = lockGroup;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public String getLockHost() {
        return lockHost;
    }

    public void setLockHost(String lockHost) {
        this.lockHost = lockHost;
    }

    public String getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
