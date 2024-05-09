package com.chengxumeng.db.result;

import com.chengxumeng.db.ResultSetHandler;
import com.chengxumeng.db.util.RowProcessor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * @program: dbutils
 * @description: Map处理器
 * @author: 程序梦
 * @create: 2024-05-08 16:05
 **/

public class MapResultHandler implements ResultSetHandler<Map<String, Object>> {
    @Override
    public Map<String, Object> handle(ResultSet rs) throws SQLException {
        return rs.next() ? RowProcessor.toMap(rs) : null;
    }

}
