package com.chengxumeng.db.result;

import com.chengxumeng.db.util.RowProcessor;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @program: dbutils
 * @description: ArrayList 多条处理器
 * @author: 程序梦
 * @create: 2024-05-10 08:33
 **/

public class ArrayListResultHandler extends AbstractListHandler<Object[]> {


    @Override
    public Object[] handleRow(ResultSet rs) throws SQLException {
        return RowProcessor.toArray(rs);
    }
}
