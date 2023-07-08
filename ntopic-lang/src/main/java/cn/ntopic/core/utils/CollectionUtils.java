package cn.ntopic.core.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;

/**
 * 集合工具类
 */
public class CollectionUtils {

    /**
     * 过滤
     *
     * @param datas 数据
     * @param <T>   类型
     * @return 过滤类型
     */
    public static <T> List<T> flitDuplicate(Collection<T> datas) {
        if (datas == null) {
            return newList();
        }
        Set<T> set = CollectionUtils.newSet();
        set.addAll(datas);
        return newList(set);
    }

    /**
     * 基于容器初始化
     *
     * @param datas 数据
     * @param <T>   泛型
     * @return 初始化的list
     */
    public static <T> List<T> newList(Collection<T> datas) {
        List<T> result = newList();
        if (isEmpty(datas)) {
            return result;
        }
        result.addAll(datas);
        return result;
    }

    /**
     * 初始化一个泛型set
     *
     * @param <T> 泛型
     * @return 10初始化的list
     */
    public static <T> Set<T> newSet() {
        return new HashSet<>(10);
    }

    /**
     * 初始化一个泛型set
     *
     * @return 10初始化的list
     */
    public static Set<String> newSet(String[] args) {
        if (args == null) {
            return newSet();
        }
        HashSet<String> set = new HashSet<>(10);
        for (String s : args) {
            set.add(s);
        }
        return set;
    }

    /**
     * 初始化一个泛型list
     *
     * @param <T> 泛型
     * @return 如果是null 就new一个list,否则原样返回
     */
    public static <T> List<T> init(List<T> list) {
        return list == null ? newList() : list;
    }

    /**
     * 初始化一个泛型list
     *
     * @param <T> 泛型
     * @return 10初始化的list
     */
    public static <T> List<T> newList() {
        return new ArrayList<>(10);
    }

    /**
     * 初始化一个泛型list
     *
     * @param <T> 泛型
     * @return 10初始化的list
     */
    public static <T> List<T> newList(T... obj) {
        List<T> objects = newList();
        if (obj == null) {
            return objects;
        }

        for (T o : obj) {
            objects.add(o);
        }
        return objects;
    }

    /**
     * 获取子list
     *
     * @param list  list
     * @param start 开始角标,0开始
     * @param size  list大小
     * @param <T>   泛型
     * @return subList
     */
    public static <T> List<T> getSubList(List<T> list, int start, int size) {
        List<T> result = newList();
        for (int i = start; i < (start + size); i++) {
            result.add(list.get(i));
        }
        return result;
    }

    /**
     * 新建一个map,hashMap
     *
     * @param <K> key类型
     * @param <V> value类型
     * @return 新map
     */
    public static <K, V> Map<K, V> newMap() {
        return new HashMap<>();
    }

    /**
     * 初始化一个泛型map
     *
     * @param <K> 泛型
     * @return 如果是null 就new一个map,否则原样返回
     */
    public static <K, V> Map<K, V> init(Map<K, V> list) {
        return list == null ? newMap() : list;
    }

    /**
     * 初始化一个泛型set
     *
     * @param <K> 泛型
     * @return 如果是null 就new一个map,否则原样返回
     */
    public static <K> Set<K> init(Set<K> set) {
        return set == null ? newSet() : set;
    }

    /**
     * 获取集合大小
     */
    public static int size(Collection coll) {
        if (isEmpty(coll)) {
            return 0;
        }

        return coll.size();
    }

    /**
     * isEmpty
     * 检测集合为空
     */
    public static boolean isEmpty(Collection coll) {
        return (coll == null || coll.isEmpty());
    }

    /**
     * 检测集合非空
     */
    public static boolean isNotEmpty(Collection coll) {
        return !isEmpty(coll);
    }

    /**
     * 获取Map大小
     */
    public static int size(Map map) {
        if (isEmpty(map)) {
            return 0;
        }

        return map.size();
    }

    /**
     * 检测包含
     */
    public static boolean contains(Collection collection, Object value) {
        return value != null && isNotEmpty(collection) && collection.contains(value);
    }

    /**
     * 检测Map为空
     */
    public static boolean isEmpty(Map map) {
        return (map == null || map.isEmpty());
    }

    /**
     * 检测Map非空
     */
    public static boolean isNotEmpty(Map map) {
        return !isEmpty(map);
    }

    /**
     * 清空集合数据
     */
    public static void clear(Collection coll) {
        if (CollectionUtils.isNotEmpty(coll)) {
            coll.clear();
        }
    }

    /**
     * 集合a - 集合b = 集合c（原集合a和集合b内容保持不变）
     */
    public static <T> List<T> subtract(Collection<T> a, Collection<T> b) {
        if (isEmpty(a)) {
            return Lists.newArrayList();
        }

        List<T> c = new ArrayList<>(a);
        if (isNotEmpty(b)) {
            c.removeAll(b);
        }

        return c;
    }

}
