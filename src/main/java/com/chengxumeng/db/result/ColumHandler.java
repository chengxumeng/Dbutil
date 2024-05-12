package com.chengxumeng.db.result;

import com.chengxumeng.db.ResultSetHandler;
import com.chengxumeng.db.util.RowProcessor;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @program: dbutils
 * @description: 列结果集处理器
 * @author: 程序梦
 * @create: 2024-05-08 15:58
 **/

public class ColumHandler<T> implements ResultSetHandler<T> {
    /**
     * 下标
     */
    private Integer columnIndex;
    /**
     * 字段名称
     */
    private String columnName;

    public ColumHandler(Integer columnIndex) {
        this.columnIndex = columnIndex;
    }

    public ColumHandler(String columnName) {
        this.columnName = columnName;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T handle(ResultSet rs) throws SQLException {
        // 判断结果集是否有数据,有进行下一步操作
        if (rs.next()) {
            if (this.columnName == null) {
                return (T) RowProcessor.toValue(rs, this.columnIndex);
            } else {
                return (T) RowProcessor.toValue(rs, this.columnName);
            }
        }
        return null;
    }
}
