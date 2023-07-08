package cn.ntopic.core.utils;

import cn.ntopic.LogConstants;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * JSON工具类
 */
public class JSONUtils {
    private final static Logger logger = LoggerFactory.getLogger(LogConstants.UTIL);

    /**
     * 类名
     */
    private static final String CLZ = JSONUtils.class.getSimpleName();

    /**
     * 对象转换为JSON字符串 <br/> 1. 缩进排版 2. 日期格式化
     */
    public static String toString(Object object) {
        SerializeWriter out = new SerializeWriter();
        try {
            JSONSerializer serializer = new JSONSerializer(out);
            serializer.config(SerializerFeature.QuoteFieldNames, true);
            serializer.config(SerializerFeature.SkipTransientField, true);
            serializer.config(SerializerFeature.PrettyFormat, true);
            serializer.config(SerializerFeature.WriteDateUseDateFormat, true);
            serializer.setDateFormat(DateUtils.FL);

            serializer.write(object);
            return out.toString();
        } finally {
            out.close();
        }
    }

    /**
     * 转换模型
     *
     * @param json json
     * @param clz  模型类型
     * @param <T>  类型
     * @return 模型
     */
    public static <T> T parse(String json, Class<T> clz) {
        return JSON.parseObject(json, clz);
    }

    /**
     * JSON格式化数据内容
     */
    public static String format(Object object) {
        if (Objects.isNull(object)) {
            return StringUtils.EMPTY;
        }

        try {
            return JSON.toJSONString(object);
        } catch (Throwable e) {
            logger.warn("[" + CLZ + "]-数据内容格式化异常-" + object, e);
            return StringUtils.EMPTY;
        }
    }

    /**
     * JSON -> Map<String,String>
     */
    public static Map<String, String> makeMapValues(String json) {
        Map<String, String> values = Maps.newLinkedHashMap();
        if (StringUtils.isBlank(json)) {
            return values;
        }

        try {
            return JSON.parseObject(json, new TypeReference<Map<String, String>>() {
            });
        } catch (Throwable e) {
            logger.warn("[" + CLZ + "]-Map数据内容解析异常-" + json, e);
            return values;
        }
    }

    /**
     * JSON -> List<String>
     */
    public static List<String> makeListValues(String json) {
        List<String> values = new ArrayList<>();
        if (StringUtils.isBlank(json)) {
            return values;
        }

        try {
            return JSON.parseObject(json, new TypeReference<List<String>>() {
            });
        } catch (Throwable e) {
            logger.warn("[" + CLZ + "]-List数据内容解析异常-" + json, e);
            return values;
        }
    }

}
