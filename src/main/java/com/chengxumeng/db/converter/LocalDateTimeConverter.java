package com.chengxumeng.db.converter;

import com.chengxumeng.db.TypeConverter;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @program: dbutils
 * @description: LocalDateTime 转换器
 * @author: 程序梦
 * @create: 2024-05-09 11:09
 **/

public class LocalDateTimeConverter implements TypeConverter {
    /**
     * 判断类型是否是LocalDateTime
     * @param type
     * @return
     */
    @Override
    public boolean support(Class<?> type) {
        return type.equals(LocalDateTime.class);
    }

    /**
     * 将数据库中的时间类型转换成LocalDateTime
     *
     * @param value     值
     * @param filedType 字段类型
     * @return
     */
    @Override
    public Object convert(Object value, Class<?> filedType) {
        return switch (value) {
            case LocalDateTime localDateTime -> localDateTime;
            case LocalDate localDate -> localDate.atStartOfDay();
            case Date sqlData -> sqlData.toLocalDate().atStartOfDay();
            case Timestamp timestamp -> timestamp.toLocalDateTime();
            case Time time -> time.toLocalTime().atDate(LocalDate.now());
            default -> throw new RuntimeException("不认识此时间类型" + value);
        };
    }
}
