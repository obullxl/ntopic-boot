/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2021 All Rights Reserved.
 */
package cn.ntopic.core.velocity;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.StringWriter;
import java.util.Map;

/**
 * Velocity工具类
 *
 * @author obullxl 2021年06月12日: 新增
 */
public class VelocityUtils {

    /**
     * 字符串渲染
     */
    public static String evaluate(String template, Map<String, Object> dataValues) {
        if (StringUtils.isBlank(template) || MapUtils.isEmpty(dataValues)) {
            return template;
        }

        // 上下文
        VelocityContext context = new VelocityContext();
        for (Map.Entry<String, Object> dataValue : dataValues.entrySet()) {
            context.put(dataValue.getKey(), dataValue.getValue());
        }

        // 字符串渲染
        StringWriter output = new StringWriter();
        Velocity.evaluate(context, output, "VelocityUtils#evaluate", template);

        return output.toString();
    }
}
