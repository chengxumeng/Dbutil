package com.chengxumeng.db;

import com.chengxumeng.db.result.*;
import com.chengxumeng.db.util.DbUtil;
import com.chengxumeng.db.util.SqlExecutor;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

/**
 * @program: dbutils
 * @description:
 * @author: 程序梦
 * @create: 2024-05-08 09:16
 **/

public class SqlExecutorTest {
    /**
     * 添加操作
     *
     * @throws SQLException
     * @throws CloneNotSupportedException
     */
    @Test
    public void test() throws SQLException, CloneNotSupportedException {
        // 获取连接对象
        SqlExecutor sqlExecutor = new SqlExecutor(DbUtil.getConnection());
        // Sql 语句
        String sql = "insert into user_info(u_name,u_password,u_age,deposit,birth) values(?,?,?,?,?)";
        // 执行添加操作
        sqlExecutor.executeUpdate(sql, "chengxumeng", "admin", 19, new BigDecimal("1000.0"), new Date());

    }

    /**
     * 批量插入数据
     */
    @Test
    public void testmain() {
        SqlExecutor sqlExecutor = new SqlExecutor(DbUtil.getConnection());
        // Sql 语句
        String sql = "insert into user_info(u_name,u_password,u_age,deposit,birth) values(?,?,?,?,?)";
        // 使用二维数组来装载数据
        Object[][] params = {
                {"chengxumeng", "admin", 19, new BigDecimal("1500.9"), new Date()},
                {"chengxumeng", "admin", 19, new BigDecimal("1500.9"), new Date()}
        };

        sqlExecutor.executeBath(sql, params);
    }

    @Test
    public void testExecuteQuery() {
        SqlExecutor sqlExecutor = new SqlExecutor(DbUtil.getConnection());
        String sql = "select * from user_info";
        ArrayResultHandler arrayResultHandler = new ArrayResultHandler();
        Object[] arrs = sqlExecutor.executeQuery(sql, arrayResultHandler);
        Arrays.stream(arrs).forEach(System.out::println);
    }

    @Test
    public void testExecuteQueryColumn() {
        SqlExecutor sqlExecutor = new SqlExecutor(DbUtil.getConnection());
        String sql = "select count(*) from user_info";
        ColumHandler<Long> handler = new ColumHandler<>(1);
        Long count = sqlExecutor.executeQuery(sql, handler);
        System.out.println(count);
    }

    @Test
    public void testExecuteQueryMap() {
        SqlExecutor sqlExecutor = new SqlExecutor(DbUtil.getConnection());
        String sql = "select * from user_info";
        MapResultHandler mapResultHandler = new MapResultHandler();
        Map<String, Object> map = sqlExecutor.executeQuery(sql, mapResultHandler);
        map.forEach((k, v) -> System.out.println(k + ":" + v));
    }

    @Test
    public void testExecuteQueryList() {
        SqlExecutor sqlExecutor = new SqlExecutor(DbUtil.getConnection());
        String sql = "select * from user_info";
        sqlExecutor.executeQuery(sql, new ListMapResultHandler()).forEach(System.out::println);
    }

    @Test
    public void testExecuteQueryBean() {
        SqlExecutor sqlExecutor = new SqlExecutor(DbUtil.getConnection());
        String sql = "select * from user_info";
        User user = sqlExecutor.executeQuery(sql, new BeanResultHandler<>(User.class));
        System.out.println(user);
    }
}
