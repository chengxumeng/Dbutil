package com.chengxumeng.db.result;

import com.chengxumeng.db.ResultSetHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: dbutils
 * @description: 抽象集合结果处理器, 用于处理多条数据集合(使用模版方法来封装处理逻辑)
 * @author: 程序梦
 * @create: 2024-05-10 08:44
 **/

public abstract class AbstractListHandler<T> implements ResultSetHandler<List<T>> {
    @Override
    public List<T> handle(ResultSet rs) throws SQLException {
        List<T> list = new ArrayList<T>();
        while (rs.next()) {
            // 直接调用下面的抽象方法, 由子类来实现不同的处理逻辑(也实现了模版方法模式也满足了开闭原则、单一职责原则、依赖倒置原则、里氏替换原则)
            list.add(handleRow(rs));
        }
        return list;
    }

    /**
     * 抽象的行处理方法,交由子类做不同的处理
     *
     * @param rs
     * @return
     * @throws SQLException
     */
    public abstract T handleRow(ResultSet rs) throws SQLException;
}
