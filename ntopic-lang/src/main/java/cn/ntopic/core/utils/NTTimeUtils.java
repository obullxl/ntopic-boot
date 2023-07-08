package cn.ntopic.core.utils;

import cn.ntopic.LogConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * 时间框架工具类
 */
public final class NTTimeUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogConstants.UTIL);

    /**
     * {@code Date}转换为{@code LocalDate}对象
     */
    public static LocalDate toLocalDate(Date date) {
        Assert.isTrue(date != null, "Date为NULL，无法转换为LocalDate对象.");
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * {@code Date}转换为{@code LocalDateTime}对象
     */
    public static LocalDateTime toLocalDateTime(Date date) {
        Assert.isTrue(date != null, "Date为NULL，无法转换为LocalDateTime对象.");
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * {@code LocalDate}转换为{@code Date}对象
     */
    public static Date toDate(LocalDate localDate) {
        Assert.isTrue(localDate != null, "LocalDate为NULL，无法转换为Date对象.");
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /**
     * {@code LocalDateTime}转换为{@code Date}对象
     */
    public static Date toDate(LocalDateTime localDateTime) {
        Assert.isTrue(localDateTime != null, "LocalDateTime为NULL，无法转换为Date对象.");
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

}
