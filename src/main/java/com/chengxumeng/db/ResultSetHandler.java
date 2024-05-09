package com.chengxumeng.db;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @description: 抽象的结果集处理器
 * 因为结果可以转换成很多种类型(可以理解这个策略接口)
 * @author: 程序梦
 * @create: 2024-05-08 09:58
 **/
public interface ResultSetHandler<T> {
    /**
     * 结果集处理方法
     *
     * @param rs 结果集对象
     * @return 处理后的对象
     * @throws SQLException sql异常
     */
    T handle(ResultSet rs) throws SQLException;
}
