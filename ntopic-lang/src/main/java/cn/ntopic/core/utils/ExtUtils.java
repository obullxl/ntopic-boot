package cn.ntopic.core.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 字符串与Map/List扩展参数工具类
 */
public class ExtUtils {
    /** 分隔符 */
    // K/V之间分隔符
    public static final String SEP_KV = "=";

    // 多个K/V对分隔符
    public static final String SEP_KVS = "|";

    // 单值列表分隔符
    public static final String SEP_LIST = "^";

    // K/V列表之间分隔符
    public static final String SEP_KVLIST = ":";

    // 多个K/V列表对分隔符
    public static final String SEP_KVLISTS = ";";

    /**
     * 新建空List对象
     */
    public static List<String> newList() {
        return new ArrayList<>();
    }

    /**
     * 新建空Map对象
     */
    public static Map<String, String> newMap() {
        return new HashMap<>();
    }

    /**
     * 检测值是否合法
     */
    public static boolean checkValue(String text) {
        return !StringUtils.contains(text, SEP_KV) && !StringUtils.contains(text, SEP_KVS);
    }

    /**
     * 美化内容
     */
    private static String beautify(String text) {
        text = StringUtils.trimToEmpty(text);

        text = StringUtils.replace(text, "\r", StringUtils.EMPTY);
        text = StringUtils.replace(text, "\n", StringUtils.EMPTY);
        text = StringUtils.replace(text, SEP_KV, "＝");
        text = StringUtils.replace(text, SEP_KVS, "｜");

        return text;
    }

    /**
     * 字符串转换为Map对象
     *
     * @param text 多个K/V对以'|'分隔，K/V间以'='分隔。
     */
    public static void toMap(Map<String, String> map, String text) {
        toMap(map, text, SEP_KVS, SEP_KV);
    }

    /**
     * 字符串转换成map
     * @param map      结果map
     * @param text     kv对
     * @param sepKVs   k/v对分隔符
     * @param sepKV    K/V间分隔符
     */
    public static void toMap(Map<String, String> map, String text, String sepKVs, String sepKV) {
        text = StringUtils.trimToEmpty(text);
        if (StringUtils.isBlank(text)) {
            return;
        }

        String[] pairs = StringUtils.split(text, sepKVs);
        if (pairs != null && pairs.length > 0) {
            for (String pair : pairs) {
                pair = StringUtils.trimToEmpty(pair);
                String[] kv = StringUtils.split(pair, sepKV, 2);
                if (kv.length > 1) {
                    map.put(StringUtils.trimToEmpty(kv[0]), StringUtils.trimToEmpty(kv[1]));
                }
            }
        }
    }

    /**
     * 字符串转换为Map对象
     *
     * @param text 多个K/V对以'|'分隔，K/V间以'='分隔。
     */
    public static Map<String, String> toMap(String text) {
        Map<String, String> map = newMap();
        toMap(map, text);

        return map;
    }

    /**
     * 字符串转换为Map对象，同ToMap，返回有顺序的
     */
    public static Map<String, String> toLinkedMap(String text) {
        Map<String, String> map = new LinkedHashMap<>();
        toMap(map, text);

        return map;
    }

    /**
     * 字符串转换为Map对象，值为List类型，多个值以‘^’分隔
     */
    public static Map<String, List<String>> toMapList(String text) {
        Map<String, List<String>> results = new LinkedHashMap<>();

        Map<String, String> temps = toLinkedMap(text);
        for (Map.Entry<String, String> temp : temps.entrySet()) {
            results.put(temp.getKey(), toValueList(temp.getValue()));
        }

        return results;
    }

    /**
     * 复杂字符串转换为Map对象，值为Map<String, List<String>>类型
     * 字符串格式： x1:y1=z1^z2|y2=z3;x2:y3=z4
     */
    public static Map<String, Map<String, List<String>>> toMapMapList(String text) {
        Map<String, Map<String, List<String>>> results = new LinkedHashMap<>();

        Map<String, String> map = new LinkedHashMap<>();
        toMap(map, text, SEP_KVLISTS, SEP_KVLIST);

        for (Map.Entry<String, String> entry : map.entrySet()) {
            results.put(entry.getKey(), toMapList(entry.getValue()));
        }
        return results;
    }

    /**
     * Map对象转换为字符串
     *
     * @param map Map对象。
     * @return 多个K/V对以'|'分隔，K/V间以'='分隔。
     */
    public static String toString(Map<String, String> map) {
        StringBuilder text = new StringBuilder(128);

        if (map == null || map.isEmpty()) {
            return text.toString();
        }

        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = StringUtils.trimToEmpty(entry.getKey());
            if (StringUtils.isBlank(key)) {
                continue;
            }

            text.append(SEP_KVS).append(key).append(SEP_KV).append(beautify(entry.getValue()));
        }

        return text.toString();
    }

    /**
     * 字符串转换为List对象
     */
    public static List<String> toList(String text, String split) {
        List<String> values = new ArrayList<>();

        String[] items = StringUtils.split(text, split);
        if (items != null) {
            for (String item : items) {
                values.add(StringUtils.trimToEmpty(item));
            }
        }

        return values;
    }

    /**
     * 字符串转换为Set对象(英文半角竖线‘|’分隔)
     */
    public static Set<String> toSet(String text) {
        return toSet(text, SEP_KVS);
    }

    /**
     * 字符串转换为Set对象
     */
    private static Set<String> toSet(String text, String split) {
        return new HashSet<>(toList(text, split));
    }

    /**
     * 字符串转换为List对象(英文半角竖线‘|’分隔)
     */
    public static List<String> toList(String text) {
        return toList(text, SEP_KVS);
    }

    /**
     * 单值字符串转换为List对象(多值‘^’分隔)
     */
    public static List<String> toValueList(String text) {
        return toList(text, SEP_LIST);
    }

    /**
     * List对象转换为字符串
     */
    public static String toString(Collection<String> items) {
        StringBuilder text = new StringBuilder(128);

        if (items == null || items.isEmpty()) {
            return text.toString();
        }

        for (String item : items) {
            text.append(SEP_KVS).append(beautify(item));
        }

        return text.toString();
    }

}
