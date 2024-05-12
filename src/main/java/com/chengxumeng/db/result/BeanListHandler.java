package com.chengxumeng.db.result;

import com.chengxumeng.db.util.RowProcessor;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @program: dbutils
 * @description: BeanListHandler 多条数据处理器, 用于处理多条数据集合(使用模版方法来封装处理逻辑)
 * @author: 程序梦
 * @create: 2024-05-10 08:55
 **/

public class BeanListHandler<T> extends AbstractListHandler<T> {

    private Class<T> beanClass;

    /**
     * 通过构造方法来初始化
     * @param beanClass
     */
    public BeanListHandler(Class<T> beanClass) {
        this.beanClass = beanClass;
    }

    @Override
    public T handleRow(ResultSet rs) throws SQLException {
        return RowProcessor.toBean(rs, beanClass);
    }
}
