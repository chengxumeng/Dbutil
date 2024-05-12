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
    /**
     * 参考 cj 老师的DButil 代码发现 如果当前处理器处理不了 他创建一个 HashMap集合去返回
     * wangl 老师 的DButil 代码发现 如果当前处理器处理不了 直接返回null
     * 两种方式都可以
     * @param rs 结果集对象
     * @return
     * @throws SQLException
     */
    @Override
    public Map<String, Object> handle(ResultSet rs) throws SQLException {
        return rs.next() ? RowProcessor.toMap(rs) : null;
    }

}
