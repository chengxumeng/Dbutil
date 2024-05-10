package com.chengxumeng.db.result;

import com.chengxumeng.db.util.RowProcessor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * @program: dbutils
 * @description: 多条数据集合
 * @author: 程序梦
 * @create: 2024-05-08 16:17
 **/

public class ListMapResultHandler extends AbstractListHandler<Map<String, Object>> {


    @Override
    public Map<String, Object> handleRow(ResultSet rs) throws SQLException {
        // 直接调用RowProcessor的toMap方法,将结果集转换为Map
        return RowProcessor.toMap(rs);
    }
}
