

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
- 工厂模式

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
 * 因为结果可以转换成很多种类型(可以理解为这个接口是一个策略接口)
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

- 把handler抽象成一个接口,好处是什么,  好处就是其他处理器可以直接实现它达到类型转换,

  而且这样更符合面向对象思想,使用策略模式来写这些结果集处理器可以让代码更富有健壮性、扩展性、更符合单一职责,一个类只为一个类型做处理,也符合开辟原则，不会去修改代码只是去扩展它。

- 我们可以看这个接口抽象方法 handler(结果集处理方法),为什么这个方法的类型是泛型,这样使用泛型让这个代码变得更富有重用性和灵活性,这个抽象方法就像模版一样实现它的类更好去扩展

- 而且 ResultSetHandler 下有很多结果处理器去继承 ResultSetHandler 接口 在SQLExeutor需要 ArrayResultHandler 可以直接使用其他处理器去代替,也满足了里氏替换

- 接口隔离原则  ResultSetHandler 下只包含一个 handle 方法 没有其他方法

- 

------

###### ArrayResultHandler

```java
/**
 * @program: dbutils
 * @description: 数组结果处理器
 * @author: 程序梦
 * @create: 2024-05-08 15:56
 **/
 public class ArrayResultHandler implements ResultSetHandler<Object[]> {
     @Override
   public Object[] handler(ResultSet rs) throws SQLException {
      // 如果结果集有参数表示true 直接调用数组转换方法去转换
     	return rs.next() ? RowProcessor.toArray(rs) : null;
   }
 }
```

- ​	ArrayResultHandler 方法是做什的,它将结果集对象转换为数组类型。
- ​    ArrayResultHandler 实现了  ResultSetHandler 接口并复写了 handler方法来做处理,也符合开辟原则和单一职责,我们这个 ArrayResultHandler 这个方法只对数组进行处理而且它进行类型转换,也不是在这个方法里面进行,它是调用了一个 RowProcessor.toArray(rs) 这个方法进行数据转换,而且可以使用继承和接口来进行扩展,不用去修改源代码
- 而且 ResultSetHandler 下有很多结果处理器去继承 ResultSetHandler 接口 在SQLExeutor需要 ArrayResultHandler 可以直接使用其他处理器去代替,也满足了里氏替换
- ArrayResultHandler 类依赖 ResultSetHandler 和  RowProcessor 没有依赖具体类去做降低了耦合度

------

RowProcessor.toArray

```java
   /**
     * 将结果集转换成数组
     *
     * @param rs 结果集对象
     * @return 返回任意类型数组
     * @throws SQLException
     */
public static Object[] toArray(ResultSet rs)throws SQLException {
  // 结果集的元数据的列数来定义数组的大小
  Object[] result = new Object[rs.getMetaData().getColumnCount()];
  // 循环当前数组的长度进行类型转换
 for (int i = 0; i < result.length; i++) {
       // 通过循环来取结果集的参数
       result[i] = rs.getObject(i + 1);
   }
  // 返回转换后的数据
  return result;
}
```

- 这个方法符合单一职责它只对数组进行转换,不做其他操作代码更好复用因为使用了泛型任何类型数组都可以进行转换
- 代码解耦:  	ArrayResultHandler 方法只需要有 RowProcessor.toArray 这个方法,不需要知道具体实现,降低耦合度
- 容易维护:如果需要修改数组对象的逻辑,只需要将RowProcessor.toArray(rs)修改为RowProcessor.toBean(rs)即可

------



###### ColumHandler

```java
public class ColumHandler <T>  implements ResultSetHandler<T> {
  // 下标
  private Integer columIndex;
  // 字段名称 
  private String  columName;
  
  /**
  * 通过构造方法去初始化参数
  */
   public ColumHandler(Integer columnIndex) {
        this.columnIndex = columnIndex;
    }

    public ColumHandler(String columnName) {
        this.columnName = columnName;
    }
		
    @Override
    @SuppressWarnings("unchecked")// 去掉转泛型的光标
 	 	public T handler (ResultSet rs) throws SQLException() {
      // 判断结果集是否有数据,有进行下一步操作
      if(rs.next()) {
         if(this.columnName == null ) {
           return (T) RowProcessor.toValue(rs,columnIndex);
         } else{
            return (T) RowProcessor.toValue(rs, this.columnName);
         }
      }
      // 如果没有为null
      return null;
    }
}
```

- ColumHandler 类的设计也符合五大设计原则：
  - 单一职责:   ColumHandler 这个类只负责处理结果集调用 RowProcessor.toColum()去处理类型转换
  - 开辟原则:  ColumHandler 类设计是开发可以使用继承和实现接口来扩展功能。
  - 里氏替换原则:而且 ResultSetHandler 下有很多结果处理器去继承 ResultSetHandler 接口 在SQLExeutor需要 ArrayResultHandler 可以直接使用 ColumHandler  去代替,也满足了里氏替换
  - 接口隔离：ColumHandler 类实现的 ResultSetHandler 接口是简单的，只包含一个 handle 方法，没有将不相关的方法放在一起。
  - 依赖倒置: ColumHandler 没有去依赖具体的实现类,它依赖的抽象的父亲和RowProcessor.toColum 方法去做操作
  - ColumHandler 提供了二种构造方法,使用列名和下标来获取结果集提供了灵活性和让用户有可选择性
  - 类型安全：ColumHandler类使用了泛型，意味着你可以传入你想要的类型不用在做类型转换

RowProcessor.toColum

```java
    /**
     * 根据列名获取某个值
     *
     * @param rs         结果集
     * @param columnName 列名
     * @return 当前列的值
     * @throws SQLException Sql异常
     */
    public static Object toValue(ResultSet rs, String columnName) throws SQLException {
        // 通过结果集的方法 getObject 来根据列名来找到数据
        return rs.getObject(columnName);
    }

    /**
     * 根据列名获取某个值
     *
     * @param rs          结果集
     * @param columnIndex 下标
     * @return 当前列的值
     * @throws SQLException Sql异常
     */
    public static Object toValue(ResultSet rs, Integer columnIndex) throws SQLException {
        // 通过结果集的方法 getObject 来根据下标来找到数据
        return rs.getObject(columnIndex);
    }
```

- 代码复用：RowProcessor.toValue 方法可以在多个地方被复用，避免了代码的重复。  
- 代码解耦：ColumHandler 类只需要知道 RowProcessor.toValue 方法的存在，而不需要知道其具体的实现，这样就降低了类之间的耦合度。  
- 易于维护：如果需要修改将结果集转换为特定类型的对象的逻辑，只需要修改 RowProcessor.toValue 方法即可，而不需要修改使用到这个方法的所有地方

------

###### MapHandler

```java
/**
 * 参考 cj 老师的DButil 代码发现 如果当前处理器处理不了 他创建一个 HashMap集合去		返回
 * wangl 老师 的DButil 代码发现 如果当前处理器处理不了 直接返回null
 * @program: dbutils
 * @description: Map处理器
 * @author: 程序梦
 * @create: 2024-05-08 16:05
 **/
public Map<String,Object> MapHandler implements ResultSetHandler<String,Object> {
  @Override
  public Map<String,Object> handle(ResultSet rs) {
    // 通过三目运算符,如果有数据调用方法,如果没有返回null
     return rs.next() ? RowProcessor.toMap(rs):null;
  }
}
```

- MapHandler 类的设计也符合五大设计原则：
  - 单一职责:   MapHandler 这个类只负责处理结果集调用 RowProcessor.toMap()去处理类型转换
  - 开辟原则:  MapHandler 类设计是开发可以使用继承和实现接口来扩展功能。
  - 里氏替换原则:而且 ResultSetHandler 下有很多结果处理器去继承 ResultSetHandler 接口 在SQLExeutor需要 ResultSetHandler 可以直接使用 MapHandler  去代替,也满足了里氏替换
  - 接口隔离：MapHandler 类实现的 ResultSetHandler 接口是简单的，只包含一个 handle 方法，没有将不相关的方法放在一起。
  - 依赖倒置: MapHandler 没有去依赖具体的实现类,它依赖的抽象的父亲和RowProcessor.toMap() 方法去做操作

RowProcessor.toMap

```java
public static Map<String,Object> toMap(ResultSet rs) throws SQLEXception {
  // 创建一个 Map 集合  用来接收转换完的数据
  Map<String,Object> map = new HashMap();
  
  // 获取元数据对象
  ResultSetMeteData meteData = rs.getMeteDate();
  
  // 循环遍历结果集
  for (int i = 0; i <meteData.getColumnCount() ; i++) {
          // 通过 meteData.getColumnLabel(i) 来获取当前列
    			String  ColumnLabel = meteData.getColumnLabel(i);
    			// 直接通过 rs.getObject(i) 获取参数 map.put 添加
    			map.put(ColumnLabel,rs.getObject(i));
  }
  retrun map;
}
```

- 代码复用：RowProcessor.toMape 方法可以在多个地方被复用，避免了代码的重复
- 代码解耦：MapHandler 类只需要知道 RowProcessor.toMap 方法的存在，而不需要知道其具体的实现，这样就降低了类之间的耦合度。  
- 易于维护：如果需要修改将结果集转换为特定类型的对象的逻辑，只需要修改 RowProcessor.toMap 方法即可，而不需要修改使用到这个方法的所有

------

###### BeanHandler

```java
/**
 * @program: dbutils
 * @description: Bean结果集处理器
 * @author: 程序梦
 * @create: 2024-05-09 08:42
 **/
public class BeanResultHandler<T> implements ResultSetHandler<T> {
  // 拿到 bean 的 class 对象
  private final Class<T> beanClass;
   /**
     * 构造方法 初始化 bean 的 class 对象
     * @param beanClass
     */
    public BeanResultHandler(Class<T> beanClass) {
        this.beanClass = beanClass;
    }
  /**
     * 处理结果集,将结果集转换为 bean 对象 创建 bean 去封装结果集中的数据
     * @param rs 结果集对象
     * @return 返回 bean 对象
     * @throws SQLException
     */
    @Override
    public T handle(ResultSet rs) throws SQLException {
      // 传入 结果集 和 bean 的 Class 对象去创建 bean
		   return rs.next() ? (T) RowProcessor.toBean(rs,beanclas);
    }
}
```

- BeanHandler 类的设计也符合五大设计原则：
  - 单一职责:   BeanHandler 这个类只负责处理结果集调用 RowProcessor.toBean()去处理类型转换
  - 开辟原则:  BeanHandler 类设计是开发可以使用继承和实现接口来扩展功能。
  - 里氏替换原则:而且 ResultSetHandler 下有很多结果处理器去继承 ResultSetHandler 接口 在SQLExeutor需要 ResultSetHandler 可以直接使用 BeanHandler  去代替,也满足了里氏替换
  - 接口隔离：MapHandler 类实现的 ResultSetHandler 接口是简单的，只包含一个 handle 方法，没有将不相关的方法放在一起。
  - 依赖倒置: MapHandler 没有去依赖具体的实现类,它依赖的抽象的父亲和RowProcessor.toBean() 方法去做操作
  - 

RowProcessor.toBean

```java
    /**
     * 将一条记录转换成实体
     */
    public static <T> T toBean(ResultSet rs, Class<T> beanClass) throws SQLException {
				// 返回创建的bean  这里使用了工厂模式但是也不完全算吧因为里面还做了日期类型转换,判断字段
        return BeanProcessor.crateBean(beanClass, rs);
    }
```

- 代码复用：该方法为泛型不管任意实体类都可以直接创建
- 单一职责:只做对 bean 的转换处理
- 工厂模式:让 crateBean 去创建bean 返回实体类
- 代码解耦：MapHandler 类只需要知道 RowProcessor.crateBean 方法的存在，而不需要知道其具体的实现，这样就降低了类之间的耦合度。  

###### BeanProcessor

```java
public class BeanProcessor {

    /**
     * SPI:寻找日期转换类
     */
    private static final ServiceLoader<TypeConverter> loader = ServiceLoader.load(TypeConverter.class);
  
  /**
     * 创建 Bean 对象
     *
     * @param clazz 实体类的class
     * @param rs    结果集
     * @param <T>
     * @return 任意对象的Bean
     * @throws SQLException 思路:
     *  1.拿到用户传过来的 entity 的 Class对象并创建对象
     *  2.通过内省的API获取属性描述器(为了符合OO思想和单一职责原则)创建 propertyDescriptor 方法来创建属性描述器,并使用try catch 捕获异常
     * 3.通过元数据获取列名,并通过 getMetaData().getColumnCount()当做循环的次数,循环遍历所有的列,通过元数据的 APi 的 getColumnLabel 来获取列名
     * 4.循环属性描述器来判断实体类的属性和数据库返回的列名是否一致,通过 hasColumnLabel 方法来判断是否一致
     * 5.如果一致那么通过 processColumn() 这个方法有二个作用
     *     5.1:获取结果集的值 来做时间类型转换,因为会要转换因为 Mysql数据库驱动5.0 版本之后返回的时间类型是 "java.util.Date","java.sql.Date","java.sql.Timestamp", "java.sql.Time"等类型而 Mysql驱动8.0版本之后返回的时间类型是 "java.time.LocalDateTime", "java.time.LocalDate", "java.time.LocalTime"等类型要进行时间日期转换而且我们转换时的类Converter类是通过SPI机制来实现的,所以我们要通过 ServiceLoader.load(TypeConverter.class)来加载转换器,并且我们使用了策略模式来实现非常符合五大范式
     *      5.2:判断属性是否是基本数据类型,如果是基本数据类型并且值是null,那么就不进行赋值,因为基本数据类型是不能为null的,如果为null会报错,所以我们要判断通过内省的getWriteMethod()方法来获取属性的set方法,然后通过反射的invoke()方法来赋值
     */
  public <T> T crateBean <T>(Class<T> clazz,ResultSet rs) throws SQLException {
    // 创建 Bean 实例
    Object entity = new Instatnce(clazz);
    // 获取到内省属性描述器
    PropertyDescriptor[] propertyDescriptors = propertyDescriptor(clazz);
    // 循环所有列,才知道数据库有多少列
    for(int i=1; i<= rs.getMetaData().getColumnCount();i++) {
      	// 获取列名 小提示:为什么使用 getColumnLabel 不使用 getColumnName
      	// 因为 getColumnLabel 可以判断 as别名  
      Stirng columnLabel = rs.getMe teData().getColumnLabel(i);
        // 循环属性描述器
            for (PropertyDescriptor ps : propertyDescriptors) {
                // 判断属性和数据库是否一致
                if (hasColumnLabel(clazz, columnLabel, ps)) {
                    // 获取rs的值
                    processColumn(rs, ps, columnLabel, entity);
                }
            }
    		}
    return (T) entity;
  }
  
      /**
     * 通过反射创建Bean实例
     *
     * @param clazz
     * @param <T>
     * @return
     */
    private static <T> T newInstatnce(Class<T> clazz) {
        T t = null;
        try {
            t = clazz.getConstructor().newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        return t;
    }
  	    /**
     * 获取属性描述器
     *
     * @param clazz
     * @return
     */
    private static PropertyDescriptor[] propertyDescriptor(Class<?> clazz) {
        try {
            // 获取属性,并忽略 Object 类的属性
            BeanInfo beanInfo = Introspector.getBeanInfo(clazz, Object.class);
            // 通过 beanInfo 获取属性描述器
            BeanDescriptor beanDescriptor = beanInfo.getBeanDescriptor();
            return beanInfo.getPropertyDescriptors();
        } catch (IntrospectionException e) {
            throw new RuntimeException("BeanInfo " + e);
        }
    }
  
   /**
     * 判断是否有实体类是否标注了 Column 注解
     *
     * @param clazz
     * @param columnName
     * @param pd
     * @return
     */
public static boolean hasColumnLable(Class<?> clazz, String columnName, PropertyDescriptor pd) throws SQLException {
       try {
            // 获取属性的名字
            String name = pd.getName();
            // 通过属性名获取实体类的属性
            Field field = null;
            // 通过内省的API获取属性的字段然后通过反射拿到属性的实例 为什么要使用反射拿到filed 因为内省不可以直接拿到
            field = clazz.getDeclaredField(name);
            // 实体类上是否有 Column 注解
            if (field.isAnnotationPresent(Column.class)) {
                // 获取注解上的值
                name = field.getAnnotation(Column.class).value();
            }
            // 比较数据库列名和实体类的属性名 equalsIgnoreCase :比较时 忽略大小写
            return name.equalsIgnoreCase(columnName);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
  }
  private static <T> void processColumn(ResultSet rs,PropertyDescriptor ps,String columnLabel,Object entity) throws SQLException {
    		// 获取 rs 的value
    		Object value = null
        // 获取 结果集的值
          value = rs.getObject(columnLabel);
        // 如果值不为空，进行类型转换
        if (value != null) {
            // 循环SPI机制加载的转换器
            for (TypeConverter typeConverter : loader) {
                // 判断时间策略转换器是否符合转换的类型
                if (typeConverter.support(ps.getPropertyType())) {
                    // 符合就进行转换
                    value = typeConverter.convert(value, ps.getPropertyType());
                    // 结束循环
                    break;
                }
            }
        }
    // 注意: 当 value 是 null 同时 字段类型是基本类型时 赋值会产生空指针异常
        // isPrimitive() 判断是否是基本数据类型  getPropertyType() 获取属性的类型

        if (ps.getPropertyType().isPrimitive() && value == null) {
            return;
        }
        try {
            // 将数据库的列名的value通过内省的API设置到实体类的属性中
            ps.getWriteMethod().invoke(entity, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
  }
}
```

- crateBean 这个方法算是简单的工厂方法,使用来创建bean
- newInstatnce:这个方法是真正用来创建Bean,每个方法只做一件事情符合单一职责
- processCloumn 方法用来进行时间类型转换,使用SPI机制来寻找,判断entity的字段是否为基本类型,如果基本数据类型,该字段如果为空,就会抛出 NullPointerException,最后通过内省对bean进行赋值.
- propertyDescriptor 方法用来获取属性构造器
- hasColumnLabel 方法用来判断实体类是否标住注解,随便进行比较
- 为什么我们通过元数据获取字段名的时候,使用是ColumLabel方法而不是ColumName呢，为什么ColumLabel方法优先判断 Select 后面的别名。

###### TypeConverter

```java
/**
 * @description: 转换器接口
 * @author: 程序梦
 * @create: 2024-05-09 10:03
 **/
public interface TypeConverter {
    /**
     * 判断是否支持转换
     *
     * @param type
     * @return
     */
    boolean support(Class<?> type);


    /**
     * 转换方法
     *
     * @param value     值
     * @param filedType 字段类型
     * @return 转换后的值
     */
    Object convert(Object value, Class<?> filedType);

}
```

- TypeConverter是一个抽象接口,也是一个策略接口

   support方法 用来判断该类型是否是有相对应的策略使用

  conver 方法用来进行类型转换.

- 我们在进行日期类型转换的时候使用了SPI来加载策略,也是使用了策略模式来优化代码让代码更具有扩展性和健壮性,

- 单一职责:TypeConverter也符合单一职责,它只做转换的功能,

- 接口隔离:TypeConverter没有什么杂七杂八的方法

------

DateConverter

```java
/**
 * @program: dbutils
 * @description: 将数据库不同类型的日期统一转换为java.util.Date
 * @author: 程序梦
 * @create: 2024-05-09 10:09
 * 因为5.0 Mysql驱动不支持LocalDateTime，LocalDate，LocalTime 所以我们得写一个转换器去把 这些类型转换成java.util.Date包括
 * "java.sql.Date",
 * "java.sql.Timestamp",
 * "java.sql.Time"
 **/
public class DateConverter implements TypeConverter {
  /**
     * 判断是否支持转换
     *
     * @param type
     * @return
     */
  @Override
  public boolean support(Class<?> type) {
    String typename = type.getName();
    retrun switch(typename) {
      // 判断 type 的全限定类名
      case : "java.util.Date",
      			 "java.sql.Data",
        		 "java.sql.Timestamp","java.sql.Time" -> ture;
      default -> false;
    }
  }
  
    /**
     * 转换时间方法
     *
     * @param value     值
     * @param filedType 字段类型
     * @return
     */
    @Override
    public Object convert(Object value, Class<?> filedType) {
        // 获取字段类型的属性类型
        String type = filedType.getName();
        long time = getTime(value);
        return switch (type) {
            case "java.util.Date" -> new Date(time);
            case "java.sql.Date" -> new java.sql.Date(time);
            case "java.sql.Timestamp" -> new Timestamp(time);
            case "java.sql.Time" -> new Time(time);
            default -> throw new RuntimeException("Unable to convert " + filedType + ".");
        };
    }

    /**
     * 需要把我们的时间类型转换成long类型(变成时间戳)
     * 使用JDK21方法 switch 语法糖
     *
     * @param value
     * @return
     */

    private long getTime(Object value) {
        return switch (value) {
            case Date date -> date.getTime();
            case LocalDateTime localDateTime -> 		            localDateTimeToDate(localDateTime).getTime();
            case LocalDate localDate ->    localDateToDate(localDate).getTime();
            case LocalTime localTime ->    localTimeToDate(localTime).getTime();
            default -> throw new RuntimeException("Unable to convert " + value.getClass() + ".");
        };
    }

    private Date localDateTimeToDate(LocalDateTime localDateTime) {
        ZoneId zoneId = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zoneId).toInstant();
        return Date.from(instant);
    }

    private Date localDateToDate(LocalDate localDate) {
        ZoneId zoneId = ZoneId.systemDefault();
        Instant instant = localDate.atStartOfDay().atZone(zoneId).toInstant();
        return Date.from(instant);
    }

    private Date localTimeToDate(LocalTime localTime) {
        ZoneId zoneId = ZoneId.systemDefault();
        Instant instant = localTime.atDate(LocalDate.now()).atZone(zoneId).toInstant();
        return Date.from(instant);
    }

}
```

- 代码解释：

  - 上面这个类继承了TypeConverter 用来类型转换,首先通过反射获取类型的路径,然后使用JDK21 的新特性swich进行类型判断如果是上面几种类型开始类型转换,在我们的BeanProcessor的ProcessColum进行调用 convert 转换时间方法进行类型转换,在Convert方法中首先通过反射获取字段的路径,然后将 BeanProcessor 的方法传来的类型进行转换成long类型,然后通过 JDK21 的新特性 swich 根据路径进行判断进行转换成对应的类型。getTime这个方法是将我们传进来的类型的值转换成long类型的时间戳。

    ![image-20240512192623043](/Users/ningmeng/Library/Application Support/typora-user-images/image-20240512192623043.png)

- 介绍 21新特性 swich做几件事情
  - 写一个案例

![image-20240512193635157](/Users/ningmeng/Library/Application Support/typora-user-images/image-20240512193635157.png)

我们可以看到上面代码解释但是我怕还不清楚我来解释一下

1.  第一步 检查传入 type 是否是 Date类型

   ```java
   if(type instanceof Data) {
     // 第二步 将 type 强行转换为 Data 类型 并赋值给 data
       data = (Data) type;
     	// 返回结果
      	return data;
   }
   ```

- 设计好处
  - 单一职责:每一个类只做一件事DateConverter 这个类只对旧的时间日期进行类型转换
  - 开辟原则: DateConverter 实现了 TypeConverter 这个接口 如果想扩展可以直接实现TypeConverter 这个接口进行其他转换,而不用对  DateConverter 进行添加方法。
  - 里氏替换:子类  DateConverter 可以完全替换它的父类,需要父类的时候可以完全使用DateConverter进行替换

------

LocalDateConverter

```java
/**
 * @program: dbutils
 * @description: LocalDate转换器
 * @author: 程序梦
 * @create: 2024-05-09 11:38
 **/
public class LocalDateConverter implements TypeConverter {
		    /**
     * 判断是否支持转换
     *
     * @param type
     * @return
     */
    @Override
    public boolean support(Class<?> type) {
        // 判断是否是LocalDate类型
        return type.equals(LocalDate.class);
    }

    /**
     * 转时间方法
     *
     * @param value     值
     * @param filedType 字段类型
     * @return 转换后的时间
     * 为什么这里不用转换time 因为time是时分秒，这个策略只需要日期(年月日)
     */
    @Override
    public Object convert(Object value, Class<?> filedType) {

        return switch (value) {
            // 如果是(本类的处理类型是LocalDate)处理本身就要转换的类型那么久直接返回处理类型
            case LocalDate localDate -> localDate;
            // LocalDateTime 转换为 localData 直接调用 toLocalDate 原理是直接获取 年月日,后面部分直接丢去
            case LocalDateTime localDataTime -> localDataTime.toLocalDate();
            // data 转换 localData 直接调用 toLocalDate 原理是 创建新的localData对象,年月日部分是data的年月日
            case Date sqldate -> sqldate.toLocalDate();
            // 直接将 timestamp 时间戳转换为 localDateTime 再转为 localData
            case Timestamp timestamp -> timestamp.toLocalDateTime().toLocalDate();
            default -> throw new RuntimeException("不认识此时间类型" + value);
        };

}
```

- 代码解释

  - 此方法实现了 TypeConverter 
    - support 判断是否是 LocalData.class 类型如果是就在 BeanProcessor 中调用Convert方法进行类型转换
    - 单一职责: 此类只对 LocalData. 类型进行转换
    - 里氏替换:在使用它实现的接口时,可以直接使用此类替换
    - 减少耦合在 BeanProcessor 时不用知道调用是那个类型转换器因为是抽象的可以

  ------

  LocalDateTimeConverter

  ```java
  /**
   * @program: dbutils
   * @description: LocalDateTime 转换器
   * @author: 程序梦
   * @create: 2024-05-09 11:09
   **/
  public class LocalDateTimeConverter implements TypeConverter {
      /**
       * 判断类型是否是LocalDateTime
       * @param type
       * @return
       */
      @Override
      public boolean support(Class<?> type) {
          return type.equals(LocalDateTime.class);
      }
  
      /**
       * 将数据库中的时间类型转换成LocalDateTime
       *
       * @param value     值
       * @param filedType 字段类型
       * @return
       */
      @Override
      public Object convert(Object value, Class<?> filedType) {
          return switch (value) {
  
              case LocalDateTime localDateTime -> localDateTime;
              case LocalDate localDate -> localDate.atStartOfDay();
              case Date sqlData -> sqlData.toLocalDate().atStartOfDay();
              case Timestamp timestamp -> timestamp.toLocalDateTime();
              case Time time -> time.toLocalTime().atDate(LocalDate.now());
              default -> throw new RuntimeException("不认识此时间类型" + value);
          };
      }
  }
  ```

  代码解释

  - 此方法实现了 TypeConverter 
    - support判断是否是 LocalDateTime 类型如果是就在 BeanProcessor  中调用Convert方法进行类型转换
    - 单一职责: 此类只对 LocalDateTime  类型进行转换
    - 里氏替换:在使用它实现的接口时,可以直接使用此类替换
    - 减少耦合在 BeanProcessor 时不用知道调用是那个类型转换器因为是抽象的可以

------

LocalTimeConverter

```java
public class LocalTimeConverter implements TypeConverter {
    /**
     * 判断实体的类型是不是LocalTime类型
     * @param type
     * @return
     */
    @Override
    public boolean support(Class<?> type) {
        return type.equals(LocalTime.class);
    }

    @Override
    public Object convert(Object value, Class<?> filedType) {

        return switch (value) {
            case LocalTime localTime -> localTime;
            case Time sqlTime -> sqlTime.toLocalTime();
            case LocalDateTime localDateTime -> localDateTime.toLocalTime();
            case Timestamp timestamp -> timestamp.toLocalDateTime().toLocalTime();
            default -> throw new RuntimeException("不认识此时间类型" + value.getClass() + ".");
        };
    }
```

代码解释

- 此方法实现了 TypeConverter 
  - support判断是否是 LocalTime 类型如果是就在 BeanProcessor  中调用Convert方法进行类型转换
  - 单一职责: 此类只对 LocalTime 类型进行转换
  - 里氏替换:在使用它实现的接口时,可以直接使用此类替换
  - 减少耦合在 BeanProcessor 时不用知道调用是那个类型转换器因为是抽象的可以

------

###### AbstractListHandler

```java
/**
 * @program: dbutils
 * @description: 抽象集合结果处理器, 用于处理多条数据集合(使用模版方法来封装处理逻辑)
 * @author: 程序梦
 * @create: 2024-05-10 08:44
 **/
public abstract Class AbstractListHandler<T> implements ResultSetHandler<List<T>> {
    
     @Override
    public List<T> handle(ResultSet rs) throws SQLException {
        List<T> list = new ArrayList<T>();
        while (rs.next()) {
            // 直接调用下面的抽象方法, 由子类来实现不同的处理逻辑(也实现了模版方法模式也满足了开闭原则、单一职责原则、依赖倒置原则、里氏替换原则)
            list.add(handleRow(rs));
        }
        // 返回 list 集合
        return list;
    }
  
  
   /**
     * 抽象的行处理方法,交由子类做不同的处理
     *
     * @param rs
     * @return
     * @throws SQLException
     */
    public abstract T handleRow(ResultSet rs) throws SQLException;
}
```

- ABstractListHandler 实现了 ResultSetHandler 实现了策略模式 
- 代码解释
  - 此类只对多条记录进行封装
    - 在 handler  里创建一个 List 集合 使用泛型来表示任意类型都可以封装成多条循环结果集 list 添加 记录,一条记录使用 handlerRow 进行转换一次,直到循环结束返回集合,这里使用了模版方法来进行优化代码,因为不同类型的参数,不同的转换,我们可以把 AbstractListHandler 这个方法比作一个模版方法封装不可变的操作,比如循环从数据查出多条记录,使用  抽象方法 handlerRow 进行转换因为这个方法是抽象方法如果我需要的是ArrList集合的参数我直接继承此方法使用 handlerRow 方法来进行类型转换 这样也符合依赖倒置 AbstractListHandler 和 ArrayListResultHandler 依赖的是 这个抽象方法 handleRow,
    - 单一职责:此类只对多条记录进行处理
    - 开辟原则:  ABstractListHandler 是一个抽象类,它只是提供了处理结果集的步骤,具体操作让它子类来处理,也符合了模版方法
    - 里氏替换需要 ArrayListResultHandler 的地方,子类都可以替换它
    - ArrayResultHandler 类只依赖于 ResultSetHandler 接口，而不依赖于具体的实现类。
    - 高层模块不应该依赖于底层模版,二者都应该依赖于抽象例如BeanListHandler 类依赖于 AbstractListHandler 抽象类，而不依赖于具体的实现类。

------

BeanListHandler

```java
/**
 * @program: dbutils
 * @description: BeanListHandler 多条数据处理器, 用于处理多条数据集合(使用模版方法来封装处理逻辑)
 * @author: 程序梦
 * @create: 2024-05-10 08:55
 **/
public class BeanListHandler<T> extends AbstractListHandler {
  
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
```

- 代码解释

  - BeanListHandler 继承  AbstractListHandler 只对 实体类多条记录进行转换
  - beanClass 用来出入给 我们的 RowProcessor.toBean用来构建我们的Bean
  - 使用构造方法来赋值
  - handleRow 这个方法进行 Bean 类型的类型转换和创建

- 设计

  - 单一职责:此类只负责将结果集创建成 Bean 并让父类进行封装成多条
  - 开辟原则: 此类继承 AbstractListHandler 它扩展了父类的功能 没有去修改父类方法此类扩展可以转换 Bean 
  - 里氏替换原则：BeanListHandler是AbstractListHandler的子类，可以在任何需要AbstractListHandler的地方使用BeanListHandler。
  - 接口隔离原则 ：BeanListHandler类只依赖于它需要的RowProcessor类的方法，不依赖于其他不需要的方法或类。
  - 依赖倒置: BeanListHandler 依赖于父类, 没有依赖具体的实现类,它只通过RowProcessor.toBean 来进行类型转换 没有直接依赖于Result 类

  ------

  ColumHandeler

  ```java
  /**
   * @program: dbutils
   * @description: 列结果集处理器
   * @author: 程序梦
   * @create: 2024-05-08 15:58
   **/
  
  public class ColumHandler<T> implements ResultSetHandler<T> {
      /**
       * 下标
       */
      private Integer columnIndex;
      /**
       * 字段名称
       */
      private String columnName;
  
      public ColumHandler(Integer columnIndex) {
          this.columnIndex = columnIndex;
      }
  
      public ColumHandler(String columnName) {
          this.columnName = columnName;
      }
  
      @Override
      @SuppressWarnings("unchecked")
      public T handle(ResultSet rs) throws SQLException {
          // 判断结果集是否有数据,有进行下一步操作
          if (rs.next()) {
              if (this.columnName == null) {
                  return (T) RowProcessor.toValue(rs, this.columnIndex);
              } else {
                  return (T) RowProcessor.toValue(rs, this.columnName);
              }
          }
          return null;
      }
  }
  ```

  -  代码解释
    - 作用:此类只对多条列数据进行处理
    - 它可以根据下标和字段名来进行,通过二个有参构造方法进行赋值
    - handler 方法 对 是字段还是下标进行判断然后调用对应的转换方法

-  设计

  - 单一职责:此类只负责将结果集创建成 Colum 并让父类进行封装成多条

  - 开辟原则: 此类继承 AbstractListHandler 它扩展了父类的功能 没有去修改父类方法此类扩展可以转换 Colum 

  - 里氏替换原则：ColumHandler是AbstractListHandler的子类，可以在任何需要AbstractListHandler的地方使用ColumHandler。

  - 接口隔离原则 ：ColumHandler类只依赖于它需要的RowProcessor类的方法，不依赖于其他不需要的方法或类。

  - 依赖倒置: BeanListHandler 依赖于父类, 没有依赖具体的实现类,它只通过RowProcessor.toValue 来进行类型转换 没有直接依赖于Result 类

------

ArrayListResultHandler

```java
/**
 * @program: dbutils
 * @description: ArrayList 多条处理器
 * @author: 程序梦
 * @create: 2024-05-10 08:33
 **/
public class ArrayListResultHandler extends AbstractListHandler  {
  	@Override
    public Object[] handleRow(ResultSet rs) throws SQLException {
        return RowProcessor.toArray(rs);
    }
}
```

-  代码解释

  -  此类只对数组进行处理
  - handleRow 这个方法 将结果集进行类型转换

- 设计

  - 单一职责:此类只负责将结果集创建成 Colum 并让父类进行封装成多条

  - 开辟原则: 此类继承 AbstractListHandler 它扩展了父类的功能 没有去修改父类方法此类扩展可以转换 Array 

  - 里氏替换原则：ArrayListResultHandler 是AbstractListHandler的子类，可以在任何需要AbstractListHandler的地方使用 ArrayListResultHandler。

  - 接口隔离原则 ：ArrayListResultHandler类只依赖于它需要的RowProcessor类的方法，不依赖于其他不需要的方法或类。

  - 依赖倒置: ArrayListResultHandler 依赖于父类, 没有依赖具体的实现类,它只通过RowProcessor.toArry 来进行类型转换 没有直接依赖于Result 类

  ------

ListMapResultHandler

```java
public class ListMapResultHandler extends AbstractListHandler<Map<String, Object>> {


    @Override
    public Map<String, Object> handleRow(ResultSet rs) throws SQLException {
        // 直接调用RowProcessor的toMap方法,将结果集转换为Map
        return RowProcessor.toMap(rs);
    }
}

```

-  代码解释

  -  此类只对Map进行处理
  - handleRow 这个方法 将结果集进行Map类型转换 然后父类将Map封装成List集合

- 设计

  - 单一职责:此类只负责将结果集创建成 Map 并让父类进行封装成多条

  - 开辟原则: 此类继承 AbstractListHandler 它扩展了父类的功能 没有去修改父类方法此类扩展可以转换 Map 

  - 里氏替换原则：ListMapResultHandler  是AbstractListHandler的子类，可以在任何需要AbstractListHandler的地方使用 ListMapResultHandler。

  - 接口隔离原则 ：ListMapResultHandler类只依赖于它需要的RowProcessor类的方法，不依赖于其他不需要的方法或类。

  - 依赖倒置: ListMapResultHandler 依赖于父类, 没有依赖具体的实现类,它只通过RowProcessor.toMap来进行类型转换 没有直接依赖于Result 类

完结！！！撒花
