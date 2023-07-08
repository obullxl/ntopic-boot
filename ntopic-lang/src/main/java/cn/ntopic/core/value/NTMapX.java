/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2021 All Rights Reserved.
 */
package cn.ntopic.core.value;

import cn.ntopic.core.builder.ToString;
import cn.ntopic.core.utils.JSONUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * ConcurrentHashMap字符串键值对
 *
 * @author obullxl 2021年06月12日: 新增
 */
public final class NTMapX extends ToString {

    /**
     * 内部Map对象
     */
    private final ConcurrentMap<String, String> ntMap;

    private NTMapX() {
        this.ntMap = new ConcurrentHashMap<>();
    }

    /**
     * 新建对象
     */
    public static NTMapX with() {
        return new NTMapX();
    }

    /**
     * 新建对象
     *
     * @param json JSON字符串
     */
    public static NTMapX with(String json) {
        if (StringUtils.isBlank(json)) {
            return with();
        }

        return with(JSONUtils.makeMapValues(json));
    }

    /**
     * 新建对象
     */
    public static NTMapX with(Map<String, String> values) {
        NTMapX ntMapX = new NTMapX();

        if (MapUtils.isEmpty(values)) {
            return ntMapX;
        }

        for (Map.Entry<String, String> value : values.entrySet()) {
            ntMapX.put(StringUtils.trimToEmpty(value.getKey()), StringUtils.trimToEmpty(value.getValue()));
        }

        return ntMapX;
    }

    /**
     * 格式化JSON字符串
     */
    public static String format(NTMapX ntMapX) {
        if (ntMapX == null) {
            return "{}";
        }

        return ntMapX.format();
    }

    /**
     * 格式化JSON字符串
     */
    public String format() {
        return JSONUtils.toString(this.ntMap);
    }

    /**
     * 内置Map对象
     */
    public ConcurrentMap<String, String> innerMap() {
        return this.ntMap;
    }

    /**
     * 元素数量
     */
    public int size() {
        return this.ntMap.size();
    }

    /**
     * 检测空元素
     */
    public boolean isEmpty() {
        return this.ntMap.isEmpty();
    }

    /**
     * 设置值
     */
    public String put(String key, String value) {
        return this.ntMap.put(StringUtils.trimToEmpty(key), Objects.toString(value));
    }

    /**
     * 删除值
     */
    public String remove(String key) {
        return this.ntMap.remove(StringUtils.trimToEmpty(key));
    }

    /**
     * 检测存在元素
     */
    public boolean containsKey(String key) {
        return this.ntMap.containsKey(StringUtils.trimToEmpty(key));
    }

    /**
     * 字符串值
     */
    public String get(String key) {
        return StringUtils.trimToEmpty(this.ntMap.getOrDefault(StringUtils.trimToEmpty(key), StringUtils.EMPTY));
    }

    /**
     * 布尔值
     */
    public boolean is(String key) {
        return BooleanUtils.toBoolean(this.get(key));
    }

    /**
     * 整数值
     */
    public int getInt(String key) {
        return NumberUtils.toInt(this.get(key));
    }

    /**
     * 长整数值
     */
    public long getLong(String key) {
        return NumberUtils.toLong(this.get(key));
    }

}
