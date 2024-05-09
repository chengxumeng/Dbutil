package com.chengxumeng.db;

/**
 * @description: 转换器接口
 * @author: 程序梦
 * @create: 2024-05-09 10:03
 **/
public interface TypeConverter {
    /**
     * 判断是否支持转换
     *
     * @param clazz
     * @return
     */
    boolean support(Class<?> type);


    /**
     * 转换方法
     *
     * @param value     值
     * @param filedType 字段类型
     * @return 转换后的值
     */
    Object convert(Object value, Class<?> filedType);

}
