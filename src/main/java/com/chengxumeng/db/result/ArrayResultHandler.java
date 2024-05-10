package com.chengxumeng.db.result;

import com.chengxumeng.db.ResultSetHandler;
import com.chengxumeng.db.util.RowProcessor;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @program: dbutils
 * @description: 数组结果处理器
 * @author: 程序梦
 * @create: 2024-05-08 15:56
 **/

public class ArrayResultHandler implements ResultSetHandler<Object[]> {
    @Override
    public Object[] handle(ResultSet rs) throws SQLException {
        return rs.next() ? RowProcessor.toArray(rs) : null;
    }
}
