/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2021 All Rights Reserved.
 */
package cn.ntopic.core.value;

import cn.ntopic.core.builder.ToString;
import cn.ntopic.core.utils.JSONUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 字符串列表
 *
 * @author obullxl 2021年06月19日: 新增
 */
public final class NTListX extends ToString {

    /**
     * 内置List对象
     */
    private final List<String> ntList;

    private NTListX() {
        this.ntList = new ArrayList<>(16);
    }

    /**
     * 新建对象
     */
    public static NTListX with() {
        return new NTListX();
    }

    /**
     * 新建对象
     *
     * @param json JSON字符串
     */
    public static NTListX with(String json) {
        if (StringUtils.isBlank(json)) {
            return with();
        }

        return with(JSONUtils.makeListValues(json));
    }

    /**
     * 新建对象
     */
    public static NTListX with(String... values) {
        NTListX ntListX = new NTListX();

        if (values == null || values.length <= 0) {
            return ntListX;
        }

        for (String value : values) {
            add(ntListX, value);
        }

        return ntListX;
    }

    /**
     * 新建对象
     */
    public static NTListX with(Collection<String> values) {
        NTListX ntListX = new NTListX();

        if (CollectionUtils.isEmpty(values)) {
            return ntListX;
        }

        for (String item : values) {
            add(ntListX, item);
        }

        return ntListX;
    }

    /**
     * 格式化JSON字符串
     */
    public static String format(NTListX ntListX) {
        if (ntListX == null) {
            return "[]";
        }

        return ntListX.format();
    }

    /**
     * 增加元素
     */
    public static boolean add(NTListX ntListX, String value) {
        if (StringUtils.isNotBlank(value)) {
            value = StringUtils.trimToEmpty(value);
            if (!ntListX.contains(value)) {
                // 增加元素
                return ntListX.add(value);
            }
        }

        return false;
    }

    /**
     * 格式化JSON字符串
     */
    public String format() {
        return JSONUtils.toString(this.ntList);
    }

    /**
     * 内容列表引用
     */
    public final List<String> innerList() {
        return this.ntList;
    }

    /**
     * 元素数量
     */
    public int size() {
        return this.ntList.size();
    }

    /**
     * 检测空元素
     */
    public boolean isEmpty() {
        return this.ntList.isEmpty();
    }

    /**
     * 检测存在元素
     */
    public boolean contains(String item) {
        if (item == null) {
            return false;
        }

        return this.ntList.contains(item);
    }

    /**
     * 增加单个元素
     */
    public boolean add(String item) {
        if (item == null) {
            return false;
        }

        return this.ntList.add(item);
    }

    /**
     * 增加多个元素
     */
    public boolean addAll(String... items) {
        if (items == null) {
            return false;
        }

        for (String item : items) {
            this.add(item);
        }

        return true;
    }

    /**
     * 增加多个元素
     */
    public boolean addAll(Collection<String> items) {
        if (items == null) {
            return false;
        }

        for (String item : items) {
            this.add(item);
        }

        return true;
    }

    /**
     * 移除单个元素
     */
    public boolean remove(String item) {
        if (item == null) {
            return false;
        }

        return this.ntList.remove(item);
    }

    /**
     * 移除多个元素
     */
    public boolean removeAll(String... items) {
        if (items == null) {
            return false;
        }

        for (String item : items) {
            this.remove(item);
        }

        return true;
    }

    /**
     * 移除多个元素
     */
    public boolean removeAll(Collection<String> items) {
        if (items == null) {
            return false;
        }

        for (String item : items) {
            this.remove(item);
        }

        return true;
    }

    /**
     * 移除所有元素
     */
    public void clear() {
        this.ntList.clear();
    }

}
