package com.chengxumeng.db.result;

import com.chengxumeng.db.ResultSetHandler;
import com.chengxumeng.db.util.RowProcessor;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @program: dbutils
 * @description: Bean结果集处理器
 * @author: 程序梦
 * @create: 2024-05-09 08:42
 **/

public class BeanResultHandler<T> implements ResultSetHandler<T> {
    /**
     * 拿到 bean 的 class 对象
      */
    private final Class<T> beanClass;

    /**
     * 构造方法 初始化 bean 的 class 对象
     * @param beanClass
     */
    public BeanResultHandler(Class<T> beanClass) {
        this.beanClass = beanClass;
    }

    /**
     * 处理结果集,将结果集转换为 bean 对象 创建 bean 去封装结果集中的数据
     * @param rs 结果集对象
     * @return 返回 bean 对象
     * @throws SQLException
     */
    @Override
    public T handle(ResultSet rs) throws SQLException {
        // 传入 结果集 和 bean 的 Class 对象去创建 bean
        return rs.next() ? (T) RowProcessor.toBean(rs, beanClass) : null;
    }
}
