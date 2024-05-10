package com.chengxumeng.db.result;

import com.chengxumeng.db.util.RowProcessor;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @program: dbutils
 * @description: 用于处理单列数据的处理器
 * @author: 程序梦
 * @create: 2024-05-10 08:59
 **/

public class ColumnListHandler<T> extends AbstractListHandler<T> {
    /**
     * 下标
     */
    private Integer columnIndex;
    /**
     * 字段名称
     */
    private String columnName;

    public ColumnListHandler(Integer columnIndex) {
        this.columnIndex = columnIndex;
    }

    public ColumnListHandler(String columnName) {
        this.columnName = columnName;
    }

    /**
     * 直接重写父类留下来的抽象方法来完成类型转换
     * 处理单行数据
     * @param rs
     * @return
     * @throws SQLException
     */
    @SuppressWarnings("unchecked")
    @Override
    public T handleRow(ResultSet rs) throws SQLException {
        if (this.columnName == null) {
            return (T) RowProcessor.toValue(rs, this.columnIndex);
        } else {
            return (T) RowProcessor.toValue(rs, this.columnName);
        }
    }
}
