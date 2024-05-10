# Dbutil 笔记

### 1、为什么要写这个框架:

- 一、 因为我们之前使用JDBC去操作数据库的时候,我们要大量操作 Connection对象(连接对象)、ResultSet对象(结果集对象)、PreparedStatement 对象(预处理)这三个对象,还要频

  繁的去关闭这三个对象

  

- 二、更好实践我们的OO思想,在写Dubtil时,非常完美实践了五大范式,使用了二种设计模式优化代码并使我们这个DButil更富有健壮性、扩展性

  

- 三、让我们之前学习的固定化思维(面向过程编程)通过这次手写改变我们的思想,让我们更好的OO的思想去编程

### 2、使用技术点和概念

- JDBC
- OO思想
- SOLID设计原则
- 类型转换
- 策略模式
- 模版方法
- SPI
- 自定义注解
- 泛型
- 内省
- JDK21 switch语法糖
- 异常处理
- 面向对象
- Maven构建工具

------



### 3、创建项目

- 打开IDEA 

![image-20240510100106613](/Users/ningmeng/Library/Application Support/typora-user-images/image-20240510100106613.png)

- 选中 -> New Project

![image-20240510100133276](/Users/ningmeng/Library/Application Support/typora-user-images/image-20240510100133276.png)

-  选择 创建 JAVA 项目 BuildSystem  我们选择 Maven  JDK 版本选择 JDKL

![image-20240510100311292](/Users/ningmeng/Library/Application Support/typora-user-images/image-20240510100311292.png)

- 等待项目构建成功后,我们来到Pom.xml 添加下方的Jar包

  ![image-20240510100420775](/Users/ningmeng/Library/Application Support/typora-user-images/image-20240510100420775.png)

- 添加项目构建信息

- ![image-20240510100745529](/Users/ningmeng/Library/Application Support/typora-user-images/image-20240510100745529.png)

- 我们前期准备工作已经完成接下来我们开始

- 我们在  Main 下创建一个 JAVA 目录, 再创建一个 你com包+你的署名+项目名

![image-20240510103131531](/Users/ningmeng/Library/Application Support/typora-user-images/image-20240510103131531.png)

#### SQL执行器

-  我们在 com.chengxumeng.db 下面创建一个 SqlExecutor 类 中文名称为 SQL执行器

  - 这个类作用是什么？

    - 这个类是用来执行SQL ,通过二个方法对我们 CRUD 操作进行封装达到通用的效果

    ![image-20240510111714282](/Users/ningmeng/Library/Application Support/typora-user-images/image-20240510111714282.png)

- 我们开始写这个类 

  -  介绍类里面的方法作用

    - ```java
       setParms(PreparedStatement ps, Object[] param) 用来设置sql语句参数
      ```

    - ```java
      executeQuery(String sql,ResultSetHandler<T> handler, Object... params) 用来做通用查询(多条,单条什么类型都可以)
      ```

    - ```java
      executeBath((String sql, Object[][] params) 这个方法用来封装批量 增删改操作
      ```

    - ```java
      executeUpdate(String sql, Object... param) 这个方法用来封装通用 增删改操作开始写
      ```


##### 1、executeUpdate

```java
/**
 * @program: dbutils
 * @description: SQL执行器, 核心入口类
 * @author: 程序梦
 * @create: 2024-05-08 08:48
 **/
public class SqlExecutor {
   // 定义连接对象用来获取用户的连接对象(定义为成员变量有二种效果第一个:更好给下方法调用。也更好的获取用户的连接对象)
  private final Connection conn;
  
   // 通过构造方法来初始化我们的连接对象
   public SqlExecutor(Connection conn) {
     // 初始化对象
     this.coon = conn;
   }
  	/**
  	* @params sql  sql 语句
  	* @param  param sql 语句需要的参数
  	* @return 返回受影响行数
  	* @throws SQLException 运行时产生SQL异常
  	*/
   public int excuteUpdate(String sql, Object... param) throwSQLException, CloneNotSupportedException {
  		// 第一步 判断连接对象是否为空 (如果为空就返回没有连接对象)
     	if(connection == null) {
        throw new  RuntimeException("Null Connection")
      }
     
     // 判断 sql 语句是否为空 或者为空字符串
     if(sql == null || sql.isEmpty()) {
        throw new RuntimeException("Null Sql statment ")
     }
     // 声明 PreparedStatement 对象
     PreparedStatement ps = null;
     // 通过 try  重抛异常
     try {
         // 预编译发送 sql 返回 ps 对象
       		ps = coonection.prepareStatement(sql);
        // 设置参数 一个是ps对象,一个是参数 
       		setParms(ps,param)
        // 执行 Sql 语句
           int i = ps.executeUpdate();
       	// 返回受影响行数
      		 return i;
     }catch(SQLException e) {
       // 异常重抛 (为什么异常重抛)
       // 1、RuntimeException是非检查异常,不需要强制捕获,可以简化代码
       // 2、通过在异常信息中附加原始,可以保留原始异常信息,而且抛出我们自定义异常更容易调试和问题定位
       throw new RuntimeException("excete fail"+e)
     } finally {
       			// 关闭ps对象
            close(ps);
       			//  关闭连接对象
            close();
        }
   }
```

- 上面方法是用来做通用增删改
- 思路:

  - 第一步：判断连接对象是否为空，如果为空就直接抛出自定义运行时异常 判断 **SQL** 是否为空,如果为空则抛出自定义运行时异常.
  - 第二步: 声明 **PreparedStatement** 对象并初始化值,使用 **connection.prepareStatement** 向数据库发送 sql 调用方法 setParms()方法做填充参数,执行 sql 语句 返回受影响行数
  - 第三步:使用 try catch 重抛异常(为什么重抛异常)因为RuntimeException是非检查异常，不需要强制捕获，可以简化代码。通过在异常信息中附加原始的SQLException异常，可以保留原始异常的详细信息，方便调试和问题定位。并使用 finally 来关闭 PreparedStatement 对象 、connection 连接对象

------

##### 2、executeBath

```java
	/**
     * 批量操作
     *
     * @param sql    sql语句
     * @param params 批量操作
     * @return 多条受影响行数
     */
  	public int [] executeBath((String sql, Object[][] params) { 
    	// 第一步 判断连接对象是否为空 (如果为空就返回没有连接对象)
     	if(connection == null) {
        throw new  RuntimeException("Null Connection")
      }
     
     // 判断 sql 语句是否为空 或者为空字符串
     if(sql == null || sql.isEmpty()) {
        throw new RuntimeException("Null Sql statment ")
     }
       // 声明 PreparedStatement 对象
     PreparedStatement ps = null;
     // 通过 try  重抛异常
     try {
         // 预编译发送 sql 返回 ps 对象
       		ps = coonection.prepareStatement(sql);
       	 // 执行批量添加参数(因为我们批量操作)
       	  for(Object[] param: params) {
            // 可以看看参数有哪些
            System.out.println(param);
            // 通过方法来设置参数
            setParms(ps,param);
            // 执行addBatch方法 (将我们的ps存入内存里面,可以减少他和数据库之间的通信,可以一次性发送给数据库执行)
            ps.addBatch()
          }
       // 批量执行()
       int[] ints = ps.executeBatch();
       // 返回受影响行数
       return ints;
    }catch(SQLException e) {
       	throw new RuntimeException("execute fail" + e);
      }
      // 关闭连接对象
      finally {
            close(ps);
            close();
        }
    }
  
    /**
     * 给sql语句设置参数
     * 注意：符合单一职责做设置参数
     * @param ps 预处理对象
     * @param param 参数
     * @throws SQLException 异常
     * 为什么需要 PreparedStatement 和 param 参数呢 因为 使用 PreparedStatement 的方法 setObject 来设置参			数	 param在这里有二个作用 1、用来做循环的次数 2、循环数组将值取出来
     */
  public void setParms(PreparedStatement ps,Object[] param) throw SQLException {	
     		for(int i =0; i<param.length;i++){
          // 根据下标来添加参数
          ps.setObject(i+1,param[]);
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
```

- 上面方法就是使用做通用批量增删改的方法
  - 问题1:为什么使用二维数组去做参数存储
  - 问题2:addBatch是什么
  - 问题3:使用addBatch的好处是什么
- 思路:
  - 1、判断 connection 是不是空,如果为空抛出自定义异常,判断 sql 如果为空抛出运行时,判断sql是不是等于空,如果为空直接返回自定义异常
  - 2、定义 PreparedStatement对象初始化,使用 **connection.prepareStatement** 向数据库发送sql ,通过 增强 For 循环传进来的参数,并添加到我们的批量缓存中
  - 执行批量添加返回受影响行数
- 答案:
  - 每一个Object[]代表一次数据库操作的所有参数，而Object[][]则代表多次数据库操作的所有参数。这样设计的目的是为了让这个方法能够一次性处理多次数据库操作，提高效率
  - 它是java.sql.PreparedStatement的一个方法，它是将SQL语句添加到批处理中方便使用executeBatch() 方法一次性执行所有的 SQL 语句。
  - 减少与数据库连接，能一次性完成SQL操作

------

##### 3、executeQuery

```java
public <T> executeQuery(String sql,ResultSetHandler<T> handler,Object... params) {
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
```

- 上面就是做通用查询的方法
  - 问题
    - 1.为什么要传入一个ResultSetHandler
    - 2.传入ResultSetHandler好处是什么
    - 3.这么写满足了什么,使用到了什么
  - 思路
    - 第   一 步：判断连接对象是否为空，如果为空就直接抛出自定义运行时异常 判断 **SQL** 是否为空,如果为空则抛出自定义运行时异常 判断处理器是否为空
    - 第二步: 声明 **PreparedStatement** 对象并初始化值,使用 **connection.prepareStatement** 向数据库发送 sql 调用方法 setParms()方法做填充参数,执行 sql 语句,得到结果集 交给我们的handler处理器来处理最后返回 用户相对应的参数
    - 第    三    步: 重抛异常,关闭 PreparedStatement ResultSet Connection。
  - 答案
    - 1.为什么传入一个ResultSetHandler这个接口,因为如果我们在查询这里写死只返一种数据类型的查询方法，接下来我们可能要写很多重复代码，比如我们这次写返回是单挑数据,当用户想要多条List<Map<String,Object>> 这种格式的数据,我们还要去写。所有这里传入一个策略接口进来优化代码
    - 传入 ResultSetHandler 接口让代码好扩展,为什么这样说会假如用户还想要其他类型查询结果可以直接实现此接口来进行转换
    - 这样写,满足了单一、接口隔离、开辟原则、使用到了策略模式来优化代码

------

#### 抽象接口结果处理器

###### 	ResultSetHandler<T>

```java
/**
 * @description: 抽象的结果集处理器
 * 因为结果可以转换成很多种类型(可以理解这个策略接口)
 * @author: 程序梦
 * @create: 2024-05-08 09:58
 **/
public interface ResultSetHandler<T> {
    /**
     * 结果集处理方法
     *
     * @param rs 结果集对象
     * @return 处理后的对象
     * @throws SQLException sql异常
     */
    T handle(ResultSet rs) throws SQLException;
}

```

- ResultSetHandler 接口是做什么,它将结果集转换为其他类型,它给其他类型处理器继承并进行结果集转换的

- 把handler抽象成一个接口,

  
