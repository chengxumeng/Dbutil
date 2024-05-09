package com.chengxumeng.db.result;

import com.chengxumeng.db.ResultSetHandler;
import com.chengxumeng.db.util.RowProcessor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @program: dbutils
 * @description: 多条数据集合
 * @author: 程序梦
 * @create: 2024-05-08 16:17
 **/

public class ListMapResultHandler implements ResultSetHandler<List<Map<String, Object>>> {
    @Override
    public List<Map<String, Object>> handle(ResultSet rs) throws SQLException {
        return RowProcessor.toList(rs);
    }
}
