package com.chengxumeng.db.util;

import com.chengxumeng.db.BeanProcessor;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: dbutils
 * @description: 行处理工具类, 将一行记录转换为 ArrayListResultHandler , Map , Bean 对象
 * @author: 程序梦
 * @create: 2024-05-08 10:06
 **/

public class RowProcessor {
    /**
     * 将结果集转换成数组
     *
     * @param rs
     * @return
     * @throws SQLException
     */
    public static Object[] toArray(ResultSet rs) throws SQLException {
        // 结果集的元数据的列数来定义数组的大小
        Object[] result = new Object[rs.getMetaData().getColumnCount()];
        for (int i = 0; i < result.length; i++) {
            // 通过循环来取结果集的参数
            result[i] = rs.getObject(i + 1);
        }
        return result;
    }

    /**
     * 根据列名获取某个值
     *
     * @param rs         结果集
     * @param columnName 列名
     * @return 当前列的值
     * @throws SQLException Sql异常
     */
    public static Object toValue(ResultSet rs, String columnName) throws SQLException {
        // 通过结果集的方法 getObject 来根据列名来找到数据
        return rs.getObject(columnName);
    }

    /**
     * 根据列名获取某个值
     *
     * @param rs          结果集
     * @param columnIndex 下标
     * @return 当前列的值
     * @throws SQLException Sql异常
     */
    public static Object toValue(ResultSet rs, Integer columnIndex) throws SQLException {
        // 通过结果集的方法 getObject 来根据下标来找到数据
        return rs.getObject(columnIndex);
    }

    /**
     * 将结果集转换成 Map
     *
     * @param rs
     * @return 返回Map
     * @throws SQLException
     */
    public static Map<String, Object> toMap(ResultSet rs) throws SQLException {
        Map<String, Object> map = new HashMap<>();

        // 获取数据源
        ResultSetMetaData metaData = rs.getMetaData();

        // 循环遍历结果集
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            //metaData.getColumnLabel(i)获取当前列的名称
            String columnLabel = metaData.getColumnLabel(i);
            //rs.getObject(i)获取当前列的值 map直接添加(将列名和值添加到map中)
            map.put(columnLabel, rs.getObject(i));
        }
        return map;
    }

    /**
     * 在循环里创建Map
     */
    public static List<Map<String, Object>> toList(ResultSet rs) throws SQLException {
        List<Map<String, Object>> list = new ArrayList<>();
        while (rs.next()) {
            Map<String, Object> map = toMap(rs);
            list.add(map);
        }
        return list;
    }

    /**
     * 将一条记录转换成实体
     */
    public static <T> T toBean(ResultSet rs, Class<T> beanClass) throws SQLException {
        // 返回创建的bean  这里使用了工厂模式但是也不完全算吧因为里面还做了日期类型转换,判断字段
        return BeanProcessor.crateBean(beanClass, rs);
    }
}
