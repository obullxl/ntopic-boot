/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2021 All Rights Reserved.
 */
package cn.ntopic.core.utils;

import cn.ntopic.core.value.NTMapX;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * @author obullxl 2021年06月19日: 新增
 */
public final class NTMapUtils {

    /**
     * CTOR
     */
    private NTMapUtils() {
    }

    /**
     * 获取{@code Date}类型
     */
    public static <K, V> Date getDate(final Map<? super K, V> map, final K key) {
        if (map == null) {
            return null;
        }

        V value = map.get(key);
        if (value == null) {
            return null;
        }

        // Date类型
        if (value instanceof Date) {
            return (Date) value;
        }

        return DateUtils.tryParseDate(Objects.toString(value));
    }

    /**
     * NTMapX元素数量
     */
    public static int size(NTMapX ntMapX) {
        return ntMapX == null ? 0 : ntMapX.size();
    }

    /**
     * NTMapX检测空元素
     */
    public static boolean isEmpty(NTMapX ntMapX) {
        return ntMapX == null || ntMapX.isEmpty();
    }

    /**
     * NTMapX获取元素
     */
    public static String getString(NTMapX ntMapX, String key) {
        return ntMapX == null ? StringUtils.EMPTY : ntMapX.get(key);
    }

}
