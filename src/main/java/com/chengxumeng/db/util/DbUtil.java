package com.chengxumeng.db.util;

import java.sql.*;

/**
 * @author 程序梦
 * @ClassName
 * @Description
 * @Annotation
 * @date 2024/4/22
 **/
public class DbUtil {
    /**
     * 连接驱动程序
     */
    static String DRIVER = "com.mysql.cj.jdbc.Driver";

    static String URL = "jdbc:mysql://127.0.0.1:3306/user?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai"; // 修改连接字符串格式

    /**
     * 帐号
     */
    static String USER = "root";
    /**
     * 密码
     */
    static String PASS = "chengxumeng";

    /**
     * 获取连接对象 -- Java程序 与 数据库之间的桥梁
     *
     * @return
     */
    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName(DRIVER); // 注册JDBC驱动程序
            conn = DriverManager.getConnection(URL, USER, PASS);
        } catch (ClassNotFoundException e) {
            System.out.println("找不到驱动程序类...");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("获取连接对象失败...");
            e.printStackTrace();
        }
        return conn;
    }
}