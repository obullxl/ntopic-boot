package cn.ntopic.core;

import cn.ntopic.LogConstants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Optional;

/**
 * 枚举基类
 */
public interface NTEnum {
    Logger LOGGER = LoggerFactory.getLogger(LogConstants.UTIL);

    /**
     * 枚举代码属性名
     */
    String CODE_NAME = "code";

    /**
     * 枚举描述属性名
     */
    String MESSAGE_NAME = "message";

    /**
     * 代码
     */
    default String getCode() {
        try {
            Field field = ReflectionUtils.findField(this.getClass(), CODE_NAME);
            field.setAccessible(true);

            return field.get(this).toString();
        } catch (Throwable e) {
            LOGGER.error("EnumBase.getCode()异常-" + e.getMessage(), e);

            throw new RuntimeException("EnumBase.getCode()异常.", e);
        }
    }

    /**
     * 描述
     */
    default String getMessage() {
        try {
            Field field = ReflectionUtils.findField(this.getClass(), MESSAGE_NAME);
            field.setAccessible(true);

            return field.get(this).toString();
        } catch (Throwable e) {
            LOGGER.error("EnumBase.getMessage()异常-" + e.getMessage(), e);

            throw new RuntimeException("EnumBase.getMessage()异常.", e);
        }
    }

    /**
     * 获取枚举对象
     */
    @SuppressWarnings("unchecked")
    static <T extends Enum<T>> Optional<T> valueOfEnum(Class<T> enumClass, String code) {
        if (StringUtils.isBlank(code)) {
            LOGGER.warn("EnumBase.valueOfEnum()-code为空.");
            return Optional.empty();
        }

        if (!NTEnum.class.isAssignableFrom(enumClass)) {
            LOGGER.warn("EnumBase.valueOfEnum()-类型不匹配-" + enumClass.getSimpleName());
            return Optional.empty();
        }

        for (T t : enumClass.getEnumConstants()) {
            NTEnum target = NTEnum.class.cast(t);

            if (target.getCode().equals(code)) {
                return Optional.of((T) target);
            }
        }

        LOGGER.warn("EnumBase.valueOfEnum()-code枚举不存在[" + code + "]");

        return Optional.empty();
    }

    /**
     * 安全获取代码
     */
    static String findEnumCode(NTEnum NTEnum) {
        if (NTEnum == null) {
            return StringUtils.EMPTY;
        }

        return NTEnum.getCode();
    }

}
