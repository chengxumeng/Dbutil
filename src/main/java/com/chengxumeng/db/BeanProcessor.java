package com.chengxumeng.db;

import java.beans.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ServiceLoader;

/**
 * @program: dbutils
 * @description: 反射工具类
 * @author: 程序梦
 * @create: 2024-05-09 08:43
 **/

public class BeanProcessor {

    /**
     * 转换器SPI
     */
    private static final ServiceLoader<TypeConverter> loader = ServiceLoader.load(TypeConverter.class);


    /**
     * 创建 Bean 对象
     *
     * @param clazz 实体类的class
     * @param rs    结果集
     * @param <T>
     * @return 任意对象的Bean
     * @throws SQLException 思路:
     *                      1.拿到用户传过来的 entity 的 Class对象并创建对象
     *                      2.通过内省的API获取属性描述器(为了符合OO思想和单一职责原则)创建 propertyDescriptor 方法来创建属性描述器,并使用try catch 捕获异常
     *                      3.通过元数据获取列名,并通过 getMetaData().getColumnCount()当做循环的次数,循环遍历所有的列,通过元数据的 APi 的 getColumnLabel 来获取列名
     *                      4.循环属性描述器来判断实体类的属性和数据库返回的列名是否一致,通过 hasColumnLabel 方法来判断是否一致
     *                      5.如果一致那么通过 processColumn() 这个方法有二个作用
     *                      5.1:获取结果集的值 来做时间类型转换,因为会要转换因为 Mysql数据库驱动5.0 版本之后返回的时间类型是 "java.util.Date", "java.sql.Date","java.sql.Timestamp", "java.sql.Time"等类型
     *                      而 Mysql驱动8.0版本之后返回的时间类型是 "java.time.LocalDateTime", "java.time.LocalDate", "java.time.LocalTime"等类型要进行时间日期转换
     *                      而且我们转换时的类Converter类是通过SPI机制来实现的,所以我们要通过 ServiceLoader.load(TypeConverter.class)来加载转换器,并且我们使用了策略模式来实现非常符合五大范式
     *                      5.2:判断属性是否是基本数据类型,如果是基本数据类型并且值是null,那么就不进行赋值,因为基本数据类型是不能为null的,如果为null会报错,所以我们要判断
     *                      通过内省的getWriteMethod()方法来获取属性的set方法,然后通过反射的invoke()方法来赋值
     */
    public static <T> T crateBean(Class<T> clazz, ResultSet rs) throws SQLException {
        // 创建Bean实例
        Object entity = newInstatnce(clazz);

        // 获取属性描述器
        PropertyDescriptor[] propertyDescriptors = propertyDescriptor(clazz);

        // 循环遍历所有的列
        for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {

            // 获取列名
            String columnLabel = rs.getMetaData().getColumnLabel(i);

            // 循环属性描述器
            for (PropertyDescriptor ps : propertyDescriptors) {
                // 判断属性和数据库是否一致
                if (hasColumnLabel(clazz, columnLabel, ps)) {
                    // 获取rs的值
                    processColumn(rs, ps, columnLabel, entity);
                }
            }
        }
        // 返回实体类
        return (T) entity;
    }

    /**
     * 通过反射创建Bean实例
     *
     * @param clazz
     * @param <T>
     * @return
     */
    private static <T> T newInstatnce(Class<T> clazz) {
        T t = null;
        try {
            t = clazz.getConstructor().newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        return t;
    }

    /**
     * 处理列
     *
     * @param rs
     * @param ps
     * @param columnLabel
     * @param <T>
     */
    private static <T> void processColumn(ResultSet rs, PropertyDescriptor ps, String columnLabel, Object entity) throws SQLException {
        // 获取rs的value
        Object value = null;

        // 获取结果集的值
        value = rs.getObject(columnLabel);
        // 如果值不为空，进行类型转换
        if (value != null) {
            // 循环SPI机制加载的转换器
            for (TypeConverter typeConverter : loader) {
                // 判断时间策略转换器是否符合转换的类型
                if (typeConverter.support(ps.getPropertyType())) {
                    // 符合就进行转换
                    value = typeConverter.convert(value, ps.getPropertyType());
                    // 结束循环
                    break;
                }
            }
        }

        // 注意: 当 value 是 null 同时 字段类型是基本类型时 赋值会产生空指针异常
        // isPrimitive() 判断是否是基本数据类型  getPropertyType() 获取属性的类型

        if (ps.getPropertyType().isPrimitive() && value == null) {
            return;
        }
        try {
            // 将数据库的列名的value通过内省的API设置到实体类的属性中
            ps.getWriteMethod().invoke(entity, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取属性描述器
     *
     * @param clazz
     * @return
     */
    private static PropertyDescriptor[] propertyDescriptor(Class<?> clazz) {
        try {
            // 获取属性,并忽略 Object 类的属性
            BeanInfo beanInfo = Introspector.getBeanInfo(clazz, Object.class);
            // 通过 beanInfo 获取属性描述器
            BeanDescriptor beanDescriptor = beanInfo.getBeanDescriptor();
            return beanInfo.getPropertyDescriptors();
        } catch (IntrospectionException e) {
            throw new RuntimeException("BeanInfo " + e);
        }
    }

    /**
     * 判断是否有实体类是否标注了 Column 注解
     *
     * @param clazz
     * @param columnName
     * @param pd
     * @return
     */
    public static boolean hasColumnLabel(Class<?> clazz, String columnName, PropertyDescriptor pd) throws SQLException {

        try {
            // 获取属性的名字
            String name = pd.getName();

            // 通过属性名获取实体类的属性
            Field field = null;
            // 通过内省的API获取属性的字段然后通过反射拿到属性的实例
            field = clazz.getDeclaredField(name);
            // 实体类上是否有 Column 注解
            if (field.isAnnotationPresent(Column.class)) {
                // 获取注解上的值
                name = field.getAnnotation(Column.class).value();
            }
            // 比较数据库列名和实体类的属性名    equalsIgnoreCase:比较时 忽略大小写
            return name.equalsIgnoreCase(columnName);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
}
