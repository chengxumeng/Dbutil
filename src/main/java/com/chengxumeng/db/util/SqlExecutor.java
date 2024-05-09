package com.chengxumeng.db.util;

import com.chengxumeng.db.ResultSetHandler;

import java.sql.*;

/**
 * @program: dbutils
 * @description: SQL执行器, 核心入口类
 * @author: 程序梦
 * @create: 2024-05-08 08:48
 **/

public class SqlExecutor {

    /**
     * 定义连接对象
     */
    private final Connection connection;

    /**
     * 构造方法传入一个 Connection 对象,并初始化对象
     *
     * @param connection
     */
    public SqlExecutor(Connection connection) {
        // 初始化对象
        this.connection = connection;
    }

    /**
     * /**
     * 执行增删改操作
     *
     * @param sql   sql语句
     * @param param sql语句所需要的参数
     * @return 返回受影响行数
     * @throws SQLException 运行时产生SQL异常
     */
    public int executeUpdate(String sql, Object... param) throws SQLException, CloneNotSupportedException {
        if (connection == null) {
            throw new RuntimeException("Null connection");
        }
        if (sql == null || sql.isEmpty()) {
            throw new RuntimeException("Null SQL statement");
        }
        // 声明 PreparedStatement 对象
        PreparedStatement ps = null;
        try {
            // 预编译发送sql返回ps对象
            ps = connection.prepareStatement(sql);
            // 设置参数
            setParms(ps, param);
            // 执行sql语句
            int i = ps.executeUpdate();
            return i;

        } catch (SQLException e) {
            // 异常重抛
            throw new RuntimeException("execute fail" + e);
        } finally {
            close(ps);
            close();
        }
    }

    /**
     * 批量操作
     *
     * @param sql    sql语句
     * @param params 批量操作
     * @return 多条受影响行数
     */
    public int[] executeBath(String sql, Object[][] params) {
        if (connection == null) {
            throw new RuntimeException("Null connection");
        }
        if (sql == null || sql.isEmpty()) {
            throw new RuntimeException("Null SQL statement");
        }
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            // 批量设置参数
            for (Object[] param : params) {
                System.out.println(param);
                setParms(ps, param);
                // 添加到批量缓存中 (将我们的 ps 操作缓存到内存,最后批量提交到数据库中)
                ps.addBatch();
            }
            // 批量执行
            int[] ints = ps.executeBatch();
            return ints;
        } catch (SQLException e) {
            throw new RuntimeException("execute fail" + e);
        }
    }

    /**
     * 给sql语句设置参数
     *
     * @param ps
     * @param param
     * @throws SQLException
     */
    private void setParms(PreparedStatement ps, Object[] param) throws SQLException {
        for (int i = 0; i < param.length; i++) {
            ps.setObject(i + 1, param[i]);
        }
    }

    /**
     * 查询
     *
     * @param sql     sql语句
     * @param handler 结果处理器
     * @param params  查询参数
     * @param <T>
     * @return 查询结果
     */
    public <T> T executeQuery(String sql, ResultSetHandler<T> handler, Object... params) {
        // 判断 connection 连接对象是否为空
        if (connection == null) {
            throw new RuntimeException("Null connection");
        }
        // 判断 SQl 语句是否为空 或者为空字符串
        if (sql == null || sql.isEmpty()) {
            throw new RuntimeException("Null Sql statement.");
        }
        // 判断处理器是否为空
        if (handler == null) {
            throw new RuntimeException("Null ResultSetHandler");
        }
        // 声明 PreparedStatement 对象 预编译对象
        PreparedStatement ps = null;
        // 声明 ResultSet 对象 结果集
        ResultSet rs = null;
        // 返回查询数据
        T result = null;
        try {
            // 预编译给数据库
            ps = connection.prepareStatement(sql);
            //设置参数
            setParms(ps, params);
            // 获取查询的结果集
            rs = ps.executeQuery();
            // 交给我们的结果集处理器去处理返回处理结果
            result = handler.handle(rs);
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(rs);
            close(ps);
            close();
        }
    }


    /**
     * 关闭statement
     *
     * @param statement
     */
    private void close(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
