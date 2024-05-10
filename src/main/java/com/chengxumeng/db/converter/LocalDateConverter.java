package com.chengxumeng.db.converter;

import com.chengxumeng.db.TypeConverter;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @program: dbutils
 * @description: LocalDate转换器
 * @author: 程序梦
 * @create: 2024-05-09 11:38
 **/

public class LocalDateConverter implements TypeConverter {
    /**
     * 判断是否支持转换
     *
     * @param type
     * @return
     */
    @Override
    public boolean support(Class<?> type) {
        // 判断是否是LocalDate类型
        return type.equals(LocalDate.class);
    }

    /**
     * 转时间方法
     *
     * @param value     值
     * @param filedType 字段类型
     * @return 转换后的时间
     * 为什么这里不用转换time 因为time是时分秒，这个策略只需要日期(年月日)
     */
    @Override
    public Object convert(Object value, Class<?> filedType) {

        return switch (value) {
            // 如果是(本类的处理类型是LocalDate)处理本身就要转换的类型那么久直接返回处理类型
            case LocalDate localDate -> localDate;
            // LocalDateTime 转换为 localData 直接调用 toLocalDate 原理是直接获取 年月日,后面部分直接丢去
            case LocalDateTime localDataTime -> localDataTime.toLocalDate();
            // data 转换 localData 直接调用 toLocalDate 原理是 创建新的localData对象,年月日部分是data的年月日
            case Date sqldate -> sqldate.toLocalDate();
            // 直接将 timestamp 时间戳转换为 localDateTime 再转为 localData
            case Timestamp timestamp -> timestamp.toLocalDateTime().toLocalDate();
            default -> throw new RuntimeException("不认识此时间类型" + value);
        };

    }
}
