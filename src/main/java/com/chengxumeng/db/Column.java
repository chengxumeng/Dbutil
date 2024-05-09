package com.chengxumeng.db;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 解决数据库字段名和实体类属性名不一致的问题
 * @author Chengxumeng
 * @Date 2024/5/9 20:00
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
    /**
     * @return value
     */
    String value() default "";
}
