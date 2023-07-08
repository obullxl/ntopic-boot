/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2020 All Rights Reserved.
 */
package cn.ntopic.das;

import cn.ntopic.LogConstants;
import cn.ntopic.core.BaseDO;
import cn.ntopic.core.utils.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.ConcurrentReferenceHashMap;
import org.springframework.util.ReflectionUtils;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Mapper数据拦截器
 * <p>
 * 1.INSERT新增DO数据拦截(DO对象继承BaseDO对象)
 *
 * @author shizihu
 * @version $Id: MapperInterceptor.java, v 0.1 2020年04月02日 16:05 shizihu Exp $
 */
@Aspect
@Component
public class DOInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogConstants.DAS);

    /**
     * 类属性缓存
     */
    private static final Map<Class<?>, List<Field>> DECLARED_FIELDS = new ConcurrentReferenceHashMap<>(256);

    /**
     * Mapper结尾insert方法拦截
     */
    @Around("execution(* ntopic.das.mapper.*Mapper.insert(..))")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        // 参数检测
        this.validate(point);

        //执行方法
        return point.proceed();
    }

    /**
     * 获取类定义属性
     *
     * @see Class#getDeclaredFields()
     */
    private List<Field> getDeclaredFields(Class<?> clazz) {
        Assert.notNull(clazz, "Class类为NULL");

        List<Field> result = DECLARED_FIELDS.get(clazz);
        if (result != null) {
            return result;
        }

        try {
            List<Field> fields = CollectionUtils.newList();

            for (Class<?> c = clazz; c != null && c != Object.class; c = c.getSuperclass()) {
                fields.addAll(Arrays.asList(c.getDeclaredFields()));
            }

            DECLARED_FIELDS.put(clazz, fields);

            return fields;
        } catch (Throwable e) {
            throw new RuntimeException("获取类属性异常[" + clazz.getName() + "]-[" + clazz.getClassLoader() + "].", e);
        }
    }

    /**
     * DO数据参数检测
     */
    private void validate(ProceedingJoinPoint point) {
        // 请求参数
        Object[] args = point.getArgs();
        if (args == null || args.length == 0) {
            // 参数为空，无需检测
            return;
        }

        Object dataObject = null;
        for (Object arg : args) {
            if (arg != null && BaseDO.class.isAssignableFrom(arg.getClass())) {
                dataObject = arg;
                break;
            }
        }

        if (dataObject == null) {
            LOGGER.info("INSERT参数无法匹配BaseDO-无需进行参数检测.");
            return;
        }

        // 参数检测
        for (Field field : this.getDeclaredFields(dataObject.getClass())) {
            field.setAccessible(true);
            Object value = ReflectionUtils.getField(field, dataObject);

            // 检测注解
            Annotation[] annotations = field.getDeclaredAnnotations();
            if (annotations.length <= 0) {
                continue;
            }

            for (Annotation annotation : annotations) {
                Class<?> clazz = annotation.getClass();

                // `NotNull`注解
                if (NotNull.class.isAssignableFrom(clazz)) {
                    this.validateNotNull(field, value);
                }

                // `Size`注解
                if (Size.class.isAssignableFrom(clazz)) {
                    this.validateSize(Size.class.cast(annotation), field, value);
                }
            }
        }
    }

    /**
     * `Size`注解
     */
    private void validateSize(Size size, Field field, Object value) {
        if (value == null) {
            return;
        }

        String name = field.getName();
        if (value instanceof CharSequence) {
            int length = StringUtils.length((CharSequence) value);

            if (size.min() > 0 && length < size.min()) {
                throw new IllegalArgumentException(name + "长度太短.");
            }

            if (size.max() > 0 && length > size.max()) {
                throw new IllegalArgumentException(name + "长度太长.");
            }
        }
    }

    /**
     * `NotNull`注解
     */
    private void validateNotNull(Field field, Object value) {
        String name = field.getName();
        if (value == null) {
            throw new IllegalArgumentException(name + "值为NULL.");
        }

        if (value instanceof CharSequence) {
            if (StringUtils.isBlank((CharSequence) value)) {
                throw new IllegalArgumentException(name + "值为空.");
            }
        }
    }

}
