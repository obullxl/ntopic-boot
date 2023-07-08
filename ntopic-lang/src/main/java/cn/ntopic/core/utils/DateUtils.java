package cn.ntopic.core.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.ntopic.LogConstants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日期工具类
 */
public final class DateUtils {
    private static final Logger logger = LoggerFactory.getLogger(LogConstants.UTIL);

    /**
     * 紧凑的日期格式：yyyyMMdd
     */
    public static final String FP = "yyyyMMdd";

    /**
     * 紧凑的日期格式长度：8
     */
    public static final int FP_LEN = StringUtils.length(FP);

    /**
     * 紧凑的月份格式：yyyyMM
     */
    public static final String MP = "yyyyMM";

    /**
     * 紧凑的月份格式长度：6
     */
    public static final int MP_LEN = StringUtils.length(MP);

    /**
     * Web的日期格式：yyyy-MM-dd
     */
    public static final String FW = "yyyy-MM-dd";

    /**
     * Web的日期格式长度：10
     */
    public static final int FW_LEN = StringUtils.length(FW);

    /**
     * Web的月份格式：yyyy-MM
     */
    public static final String MW = "yyyy-MM";

    /**
     * Web的月份格式长度：7
     */
    public static final int MW_LEN = StringUtils.length(MW);

    /**
     * 超长的日期格式：yyyy-MM-dd HH:mm:ss
     */
    public static final String FL = "yyyy-MM-dd HH:mm:ss";

    /**
     * 默认的时间格式
     */
    public static final String FD = "yyyyMMddHHmmss";

    /**
     * yyyyMMddHHmm时间格式
     */
    public static final String YMDHM = "yyyyMMddHHmm";

    /**
     * 带毫秒的日期格式：yyyy-MM-dd HH:mm:ss.SSS
     */
    public static final String FLS = "yyyy-MM-dd HH:mm:ss.SSS";

    /**
     * 日期
     */
    public static final String DATE = "DATE";

    /**
     * 月份
     */
    public static final String MONTH = "MONTH";

    /**
     * 默认日期
     */
    public static Date newDefaultDate() {
        return DateUtils.parseFW("1988-08-08");
    }

    /**
     * 日期格式器
     */
    public static DateFormat newDateFormat(String fmt) {
        return new SimpleDateFormat(fmt);
    }

    /**
     * 同一天,同一年
     *
     * @param test   时间
     * @param source 时间
     * @return 是否同一天
     */
    public static boolean isSameDay(Date test, Date source) {
        if (test == null || source == null) {
            return false;
        }
        return StringUtils.equals(formatFP(test), formatFP(source));
    }

    /**
     * 返回一个时间是否在时间区间内
     * 闭区间
     *
     * @param test  测试的时间
     * @param begin 开始时间
     * @param end   结束时间
     * @return 是否是区间内, 如果有null返回false
     */
    public static boolean isInDateArray(Date test, Date begin, Date end) {
        if (test == null || begin == null || end == null) {
            return false;
        }
        //包含就返回true
        if (test.equals(begin) || test.equals(end)) {
            return true;
        }

        return test.after(begin) && test.before(end);
    }

    /**
     * 获取当前月的第一时间 (0秒)
     *
     * @return 时间
     */
    public static Date monthBegin() {
        String mounthStr = format(new Date(), MP);
        return toDate(mounthStr, MP);
    }

    /**
     * 获取当前月的第一时间 (0秒)
     *
     * @return 时间
     */
    public static Date monthBegin(Date date) {
        String mounthStr = format(date, MP);
        return toDate(mounthStr, MP);
    }

    /**
     * 获取当前月的最后一刻 (秒级别)
     *
     * @return 时间
     */
    public static Date monthEnd() {
        String mounthStr = format(new Date(), MP);
        Date date = toDate(mounthStr, MP);
        Date date1 = DateUtils.addMonths(date, 1);
        return DateUtils.addSeconds(date1, -1);
    }

    /**
     * 获取当前月的最后一刻 (秒级别)
     *
     * @return 时间
     */
    public static Date monthEnd(Date month) {
        String mounthStr = format(month, MP);
        Date date = toDate(mounthStr, MP);
        Date date1 = DateUtils.addMonths(date, 1);
        return DateUtils.addSeconds(date1, -1);
    }

    /**
     * 尝试解析时间。
     * <p/>
     * yyyyMMdd -> yyyy-MM-dd -> yyyyMMddHHmmss -> yyyy-MM-dd HH:mm:ss
     */
    public static Date tryParseDate(String dateValue) {
        if (StringUtils.isBlank(dateValue)) {
            return null;
        }

        int length = StringUtils.length(dateValue);
        try {
            if (length == StringUtils.length(FP)) {
                Date value = parseFP(dateValue);
                if (value != null) {
                    return value;
                }
            }

            if (length == StringUtils.length(FW)) {
                Date value = parseFW(dateValue);
                if (value != null) {
                    return value;
                }
            }

            if (length == StringUtils.length(FD)) {
                Date value = parseFD(dateValue);
                if (value != null) {
                    return value;
                }
            }

            if (length == StringUtils.length(FL)) {
                Date value = parseFL(dateValue);
                if (value != null) {
                    return value;
                }
            }

            // 未知日期格式
            return null;
        } catch (Throwable e) {
            logger.warn("字符串[" + dateValue + "]尝试转换为日期Date类型异常.", e);
            return null;
        }
    }

    /**
     * 格式化
     */
    public static String format(Date date, String format) {
        if (date == null || StringUtils.isBlank(format)) {
            return StringUtils.EMPTY;
        }

        return newDateFormat(format).format(date);
    }

    /**
     * 时间解析
     */
    public static Date parse(String value, String format) {
        if (StringUtils.isBlank(value) || StringUtils.isBlank(format)) {
            return null;
        }

        try {
            return newDateFormat(format).parse(value);
        } catch (Throwable e) {
            logger.warn("字符串[{}]尝试转换为Date类型[{}]异常.", value, format, e);
            return null;
        }
    }

    /**
     * 获取上周一的时间
     *
     * @return yyyyMMdd
     */
    public static String lastWeekStr() {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(new Date());
        calendar1.add(Calendar.WEEK_OF_YEAR, -1);
        calendar1.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return DateUtils.formatFP(calendar1.getTime());
    }

    /**
     * 格式化（yyyyMMdd）
     */
    public static String formatFP(Date date) {
        if (date == null) {
            return StringUtils.EMPTY;
        }

        return newDateFormat(FP).format(date);
    }

    /**
     * 格式化（yyyyMMddHHmm）
     *
     * @param date 日期
     * @return 格式化后的日期字符串
     */
    public static String formatYMDHM(Date date) {
        if (date == null) {
            return StringUtils.EMPTY;
        }
        return newDateFormat(YMDHM).format(date);
    }

    /**
     * 格式化（yyyyMM）
     */
    public static String formatMP(Date date) {
        if (date == null) {
            return StringUtils.EMPTY;
        }

        return newDateFormat(MP).format(date);
    }

    /**
     * 格式化（yyyy-MM-dd）
     */
    public static String formatFW(Date date) {
        if (date == null) {
            return StringUtils.EMPTY;
        }

        return newDateFormat(FW).format(date);
    }

    /**
     * 格式化（yyyy-MM）
     */
    public static String formatMW(Date date) {
        if (date == null) {
            return StringUtils.EMPTY;
        }

        return newDateFormat(MW).format(date);
    }

    /**
     * 格式解析（yyyyMMdd）
     */
    public static Date parseFP(String date) {
        if (StringUtils.isBlank(date)) {
            return null;
        }

        try {
            return newDateFormat(FP).parse(date);
        } catch (Exception e) {
            logger.warn("[日期工具]-解析日期异常, DTStr[" + date + "], Format[" + FW + "].");
        }

        return null;
    }

    /**
     * 格式解析（yyyy-MM-dd）
     */
    public static Date parseFW(String date) {
        if (StringUtils.isBlank(date)) {
            return null;
        }

        try {
            return newDateFormat(FW).parse(date);
        } catch (Exception e) {
            logger.warn("[日期工具]-解析日期异常, DTStr[" + date + "], Format[" + FW + "].");
        }

        return null;
    }

    /**
     * 格式解析（yyyyMM）
     */
    public static Date parseMP(String date) {
        if (StringUtils.isBlank(date)) {
            return null;
        }

        try {
            return newDateFormat(MP).parse(date);
        } catch (Exception e) {
            logger.warn("[日期工具]-解析月份异常, DTStr[" + date + "], Format[" + MP + "].");
        }

        return null;
    }

    /**
     * 格式解析（yyyy-MM）
     */
    public static Date parseMW(String date) {
        if (StringUtils.isBlank(date)) {
            return null;
        }

        try {
            return newDateFormat(MW).parse(date);
        } catch (Exception e) {
            logger.warn("[日期工具]-解析月份异常, DTStr[" + date + "], Format[" + MW + "].");
        }

        return null;
    }

    /**
     * 格式化（yyyyMMddHHmmss）
     */
    public static String formatFD(Date date) {
        if (date == null) {
            return StringUtils.EMPTY;
        }

        return newDateFormat(FD).format(date);
    }

    /**
     * 格式化（yyyy-MM-dd HH:mm:ss）
     */
    public static String formatFL(Date date) {
        if (date == null) {
            return StringUtils.EMPTY;
        }

        return newDateFormat(FL).format(date);
    }

    /**
     * 格式化（yyyy-MM-dd HH:mm:ss.SSS）
     */
    public static String formatFLS(Date date) {
        if (date == null) {
            return StringUtils.EMPTY;
        }

        return newDateFormat(FLS).format(date);
    }

    /**
     * 格式解析（yyyy-MM-dd HH:mm:ss.SSS）
     */
    public static Date parseFLS(String dtstr) {
        if (StringUtils.isBlank(dtstr)) {
            return null;
        }
        try {
            return newDateFormat(FLS).parse(dtstr);
        } catch (Exception e) {
            logger.warn("[日期工具]-解析日期异常, DTStr[" + dtstr + "], Format[" + FLS + "].");
        }

        return null;
    }

    /**
     * 格式解析（yyyyMMddHHmmss）
     */
    public static Date parseFD(String dtstr) {
        try {
            return newDateFormat(FD).parse(dtstr);
        } catch (Exception e) {
            logger.warn("[日期工具]-解析日期异常, DTStr[" + dtstr + "], Format[" + FD + "].");
        }

        return null;
    }

    /**
     * 格式解析（yyyy-MM-dd HH:mm:ss）
     */
    public static Date parseFL(String dtstr) {
        try {
            return newDateFormat(FL).parse(dtstr);
        } catch (Exception e) {
            logger.warn("[日期工具]-解析日期异常, DTStr[" + dtstr + "], Format[" + FL + "].", e);
        }

        return null;
    }

    /**
     * 格式解析（yyyy-MM-dd HH:mm:ss）
     */
    public static java.sql.Timestamp parseSQLFL(String dtstr) {
        Date date = parseFL(dtstr);

        if (date == null) {
            return null;
        }

        return new java.sql.Timestamp(date.getTime());
    }

    /**
     * DB时间戳
     */
    public static java.sql.Timestamp toTimestamp(Date date) {
        if (date == null) {
            return null;
        }

        return new java.sql.Timestamp(date.getTime());
    }

    /**
     * 根据秒截取(毫秒设置为0)
     */
    public static Date truncateBySecond(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        c.set(Calendar.MILLISECOND, 0);

        return c.getTime();
    }

    /**
     * 根据日期截取
     */
    public static Date truncateByDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        return c.getTime();
    }

    /**
     * 根据月份截取
     *
     * @return 上个月的最后1天
     */
    public static Date truncateByMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        c.set(Calendar.DAY_OF_MONTH, 0);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        return c.getTime();
    }

    /**
     * 增加毫秒
     */
    public static Date addMillis(Date date, int millis) {
        if (date == null) {
            return null;
        }

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MILLISECOND, millis);
        return c.getTime();
    }

    /**
     * 增加秒钟
     */
    public static Date addSeconds(Date date, int seconds) {
        if (date == null) {
            return null;
        }

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.SECOND, seconds);
        return c.getTime();
    }

    /**
     * 增加分钟
     */
    public static Date addMinutes(Date date, int minutes) {
        if (date == null) {
            return null;
        }

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MINUTE, minutes);
        return c.getTime();
    }

    /**
     * 增加小时
     */
    public static Date addHours(Date date, int hours) {
        if (date == null) {
            return null;
        }

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.HOUR_OF_DAY, hours);
        return c.getTime();
    }

    /**
     * 增加天数
     */
    public static Date addDays(Date date, int days) {
        if (date == null) {
            return null;
        }

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH, days);
        return c.getTime();
    }

    /**
     * 增加月份
     */
    public static Date addMonths(Date date, int months) {
        if (date == null) {
            return null;
        }

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, months);
        return c.getTime();
    }

    /**
     * 增加年
     */
    public static Date addYears(Date date, int years) {
        if (date == null) {
            return null;
        }

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.YEAR, years);
        return c.getTime();
    }

    /**
     * 日期间秒数
     */
    public static int diffSeconds(Date one, Date two) {
        if (one == null || two == null) {
            return 0;
        }

        return Math.abs((int) ((one.getTime() - two.getTime()) / org.apache.commons.lang3.time.DateUtils.MILLIS_PER_SECOND));
    }

    /**
     * 日期间分钟数
     */
    public static int diffMinutes(Date one, Date two) {
        if (one == null || two == null) {
            return 0;
        }

        return Math.abs((int) ((one.getTime() - two.getTime()) / org.apache.commons.lang3.time.DateUtils.MILLIS_PER_MINUTE));
    }

    /**
     * 日期间天数
     */
    public static int diffDays(Date one, Date two) {
        if (one == null || two == null) {
            return 0;
        }

        return Math.abs((int) ((one.getTime() - two.getTime()) / org.apache.commons.lang3.time.DateUtils.MILLIS_PER_DAY));
    }

    /**
     * 获取小时值，24小时制(从‘0’到‘23’)
     */
    public static int findHour(Date date) {
        if (date == null) {
            return -1;
        }

        Calendar c = Calendar.getInstance();
        c.setTime(date);

        return c.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 获取月份值，从‘0’到‘11’
     */
    public static int findMonth(Date date) {
        if (date == null) {
            return -1;
        }

        Calendar c = Calendar.getInstance();
        c.setTime(date);

        return c.get(Calendar.MONTH);
    }

    /**
     * 获取日值，从‘1’到‘31’
     */
    public static int findDay(Date date) {
        if (date == null) {
            return -1;
        }

        Calendar c = Calendar.getInstance();
        c.setTime(date);

        return c.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 按照指定的格式把Date格式化成String类型
     */
    public static String toString(Date date, String fstr) {
        try {
            DateFormat format = newDateFormat(fstr);
            return format.format(date);
        } catch (Exception e) {
            logger.warn("格式化日期异常, Date:" + date + ", Format:" + fstr);
        }

        return null;
    }

    /**
     * 把String还原成Date类型
     */
    public static Date toDate(String dstr) {
        int length = StringUtils.length(dstr);

        Date date = null;
        if (length == 4) {
            date = toDate(dstr, "yyyy");
        } else if (length == 6) {
            date = toDate(dstr, "yyyyMM");
        } else if (length == 8) {
            date = toDate(dstr, "yyyyMMdd");
        } else if (length == 10) {
            date = toDate(dstr, "yyyy-MM-dd");
        } else if (length == 14) {
            date = toDate(dstr, FD);
        } else if (length == 19) {
            date = toDate(dstr, FL);
        }

        return date;
    }

    /**
     * 按照指定的格式把String还原成Date类型
     */
    public static Date toDate(String dstr, String fstr) {
        try {
            DateFormat format = newDateFormat(fstr);
            return format.parse(dstr);
        } catch (Exception e) {
            logger.warn("格式化日期异常, DtStr:" + dstr + ", Format:" + fstr);
        }

        return null;
    }

    /**
     * 判断两个时间相同
     *
     * @param date1 时间1
     * @param date2 时间2
     * @return 是否相同
     */
    public static boolean equals(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return false;
        }
        return date1.equals(date2);
    }

    /**
     * 当前日期月份的最后1天
     */
    public static Date monthLastDate(Date date) {
        if (date == null) {
            return null;
        }

        // 本日期
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        // 新日期
        Calendar n = Calendar.getInstance();
        n.set(Calendar.YEAR, c.get(Calendar.YEAR));
        n.set(Calendar.MONTH, c.get(Calendar.MONTH) + 1);
        n.set(Calendar.DAY_OF_MONTH, 0);
        n.set(Calendar.HOUR_OF_DAY, 0);
        n.set(Calendar.MINUTE, 0);
        n.set(Calendar.SECOND, 0);
        n.set(Calendar.MILLISECOND, 0);

        return n.getTime();
    }

    /**
     * 当前日期月份的最后1天
     */
    public static Date dateMaxTimeFP(String date) {
        Date dateTime = parseFP(date);
        if (dateTime == null) {
            return null;
        }

        dateTime = addDays(dateTime, 1);
        dateTime = addMillis(dateTime, -1);

        return dateTime;
    }

    /**
     * 获取间隔日期天数
     */
    public static List<String> dateRangeFP(String minDate, String maxDate) {
        List<String> dates = new ArrayList<String>();

        Date minDateValue = parseFP(minDate);
        Date maxDateValue = parseFP(maxDate);

        if (minDateValue == null || maxDateValue == null) {
            return dates;
        }

        while (maxDateValue.after(minDateValue)) {
            dates.add(formatFP(minDateValue));
            minDateValue = addDays(minDateValue, 1);
        }

        dates.add(maxDate);

        return dates;
    }

    /**
     * 按日或按月往前推，获得比较值日期。
     */
    public static String findCompareDate(String bizDate, List<String> skipDates, String normPeriod) {
        String date = bizDate;
        while (StringUtils.equalsIgnoreCase(date, bizDate)) {
            if (CollectionUtils.isNotEmpty(skipDates) && skipDates.contains(date)) {
                continue;
            }
            // 往前推一个日期
            if (StringUtils.equalsIgnoreCase(normPeriod, DATE)) {
                date = DateUtils.formatFP(DateUtils.addDays(DateUtils.parseFP(date), -1));
            } else if (StringUtils.equalsIgnoreCase(normPeriod, MONTH)) {
                date = DateUtils.formatFP(DateUtils.monthLastDate(DateUtils.addMonths(
                        DateUtils.parseFP(date), -1)));
            }
        }
        return date;
    }

    /**
     * 获取指定数量业务日期：按日或按月往前推，获得指定数量的所有业务日期。
     */
    public static List<String> findLastBizDateFP(int count, String maxDate, List<String> skipDates,
                                                 String normPeriod) {
        List<String> bizDates = new ArrayList<String>();

        String date = maxDate;
        while (bizDates.size() < count) {
            if (skipDates == null || !skipDates.contains(date)) {
                bizDates.add(date);
            }

            // 往前推一个日期
            if (normPeriod == DATE) {
                date = DateUtils.formatFP(DateUtils.addDays(DateUtils.parseFP(date), -1));
            } else if (normPeriod == MONTH) {
                date = DateUtils.formatFP(DateUtils.monthLastDate(DateUtils.addMonths(
                        DateUtils.parseFP(date), -1)));
            }
        }

        return bizDates;
    }

    /**
     * 当天日期，yyyyMMdd格式
     */
    public static String todayFP() {
        return formatFP(new Date());
    }

    /**
     * 判断某个时刻是否在一段时间之间（闭区间）
     *
     * @param start     区间开始时间(包含)
     * @param finish    区间结束时间(包含)
     * @param checkDate 检测时间
     */
    public static boolean inDateRange(Date start, Date finish, Date checkDate) {
        if (start == null || finish == null || checkDate == null) {
            return false;
        }

        return !start.after(checkDate) && !checkDate.after(finish);
    }

}
