package com.chengxumeng.db.converter;

import com.chengxumeng.db.TypeConverter;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.*;
import java.util.Date;

/**
 * @program: dbutils
 * @description: 将数据库不同类型的日期统一转换为java.util.Date
 * @author: 程序梦
 * @create: 2024-05-09 10:09
 * 因为5.0 Mysql驱动不支持LocalDateTime，LocalDate，LocalTime 所以我们得写一个转换器去把 这些类型转换成java.util.Date包括
 * "java.sql.Date",
 * "java.sql.Timestamp",
 * "java.sql.Time"
 **/

public class DateConverter implements TypeConverter {
    /**
     * 判断是否支持转换
     *
     * @param type
     * @return
     */
    @Override
    public boolean support(Class<?> type) {
        // 判断type的全限定类名
        String typename = type.getName();
        return switch (typename) {
            case "java.util.Date",
                 "java.sql.Date",
                 "java.sql.Timestamp", "java.sql.Time" -> true;
            default -> false;
        };
    }

    /**
     * 转换时间方法
     *
     * @param value     值
     * @param filedType 字段类型
     * @return
     */
    @Override
    public Object convert(Object value, Class<?> filedType) {
        // 获取字段类型的属性类型
        String type = filedType.getName();
        long time = getTime(value);
        return switch (type) {
            case "java.util.Date" -> new Date(time);
            case "java.sql.Date" -> new java.sql.Date(time);
            case "java.sql.Timestamp" -> new Timestamp(time);
            case "java.sql.Time" -> new Time(time);
            default -> throw new RuntimeException("Unable to convert " + filedType + ".");
        };
    }

    /**
     * 需要把我们的时间类型转换成long类型(变成时间戳)
     * 使用JDK21方法 switch 语法糖
     *
     * @param value
     * @return
     */

    private long getTime(Object value) {
        return switch (value) {
            case Date date -> date.getTime();
            case LocalDateTime localDateTime -> localDateTimeToDate(localDateTime).getTime();
            case LocalDate localDate -> localDateToDate(localDate).getTime();
            case LocalTime localTime -> localTimeToDate(localTime).getTime();
            default -> throw new RuntimeException("Unable to convert " + value.getClass() + ".");
        };
    }

    private Date localDateTimeToDate(LocalDateTime localDateTime) {
        ZoneId zoneId = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zoneId).toInstant();
        return Date.from(instant);
    }

    private Date localDateToDate(LocalDate localDate) {
        ZoneId zoneId = ZoneId.systemDefault();
        Instant instant = localDate.atStartOfDay().atZone(zoneId).toInstant();
        return Date.from(instant);
    }

    private Date localTimeToDate(LocalTime localTime) {
        ZoneId zoneId = ZoneId.systemDefault();
        Instant instant = localTime.atDate(LocalDate.now()).atZone(zoneId).toInstant();
        return Date.from(instant);
    }


}
