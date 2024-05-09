package com.chengxumeng.db.converter;

import com.chengxumeng.db.TypeConverter;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * @program: dbutils
 * @description: LocalTime转换器
 * @author: 程序梦
 * @create: 2024-05-09 11:48
 **/

public class LocalTimeConverter implements TypeConverter {
    /**
     * 判断实体的类型是不是LocalTime类型
     * @param type
     * @return
     */
    @Override
    public boolean support(Class<?> type) {
        return type.equals(LocalTime.class);
    }

    @Override
    public Object convert(Object value, Class<?> filedType) {

        return switch (value) {
            case LocalTime localTime -> localTime;
            case Time sqlTime -> sqlTime.toLocalTime();
            case LocalDateTime localDateTime -> localDateTime.toLocalTime();
            case Timestamp timestamp -> timestamp.toLocalDateTime().toLocalTime();
            default -> throw new RuntimeException("不认识此时间类型" + value.getClass() + ".");
        };
    }
}
