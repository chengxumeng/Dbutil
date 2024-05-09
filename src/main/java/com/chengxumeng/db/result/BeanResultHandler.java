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

    private final Class<T> beanClass;

    public BeanResultHandler(Class<T> beanClass) {
        this.beanClass = beanClass;
    }

    @Override
    public T handle(ResultSet rs) throws SQLException {
        return rs.next() ? (T) RowProcessor.toBean(rs, beanClass) : null;
    }
}
