# MyBatis

- 优秀的**持久层框架**

### 如何获得Mybatis

github和Maven仓库

- Maven引入语句

```xml
<dependency>
    <groupId>org.mybatis</groupId>
    <artifactId>mybatis</artifactId>
    <version>3.5.2</version>
</dependency>
```

### 什么是持久化

- 做数据持久化

- 持久化就是将程序的数据在持久状态和瞬时状态转化的过程
- 将这些想要保存下来的数据放入数据库（JDBC），放入文件（IO）



### Mybatis程序入门

思路：搭建环境--->导入Mybatis--->编写代码--->测试

#### 2.1搭建环境

- 数据库

```
设置时区
SET GLOBAL time_zone='+8:00';
```



```sql
CREATE DATABASE `mybatis`;

USE `mybatis`;

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
`id` INT(20) NOT NULL,
`name` VARCHAR(30) DEFAULT NULL,
`pwd` VARCHAR(30) DEFAULT NULL,
PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

INSERT  INTO `user`(`id`,`name`,`pwd`) VALUES (1,'狂神','123456'),(2,'张三','abcdef'),(3,'李四','987654');
```

- 新建项目

创建完maven子项目之后一定要确保如下图用的是自己的maven

<img src="C:\Users\admin\AppData\Roaming\Typora\typora-user-images\image-20220510001854039.png" alt="image-20220510001854039" style="zoom: 25%;" />

删除src目录，之后这个工程就变成了一个父工程了

在父工程中的pom文件导入一些列依赖（mybatis，mysql，junit）

```xml
<dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis</artifactId>
            <version>3.5.2</version>
        </dependency>
<!--导入Mysiql-->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>5.1.47</version>
        </dependency>

        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.12</version>
        </dependency>
```

- 创建一个模块

  - 编写mybatis核心配置文件

    - 在resource下键一个新的xml文件用来配置mybatis（连接数据库）

    ```xml
    <?xml version="1.0" encoding="UTF-8" ?>
    <!DOCTYPE configuration
      PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
      "http://mybatis.org/dtd/mybatis-3-config.dtd">
    <configuration>
      <environments default="development">
        <environment id="development">
          <transactionManager type="JDBC"/>
          <dataSource type="POOLED">
            <property name="driver" value="${driver}"/>
            <property name="url" value="${url}"/>
            <property name="username" value="${username}"/>
            <property name="password" value="${password}"/>
          </dataSource>
        </environment>
      </environments>
      <mappers>
        <mapper resource="org/mybatis/example/BlogMapper.xml"/>
      </mappers>
    </configuration>
    ```

  - 编写mybatis的工具类（为了获取sqlsession对象）

    每个基于 MyBatis 的应用都是以一个 SqlSessionFactory 的实例为核心的。SqlSessionFactory 的实例可以通过 SqlSessionFactoryBuilder 获得。而 SqlSessionFactoryBuilder 则可以从 XML 配置文件或一个预先配置的 Configuration 实例来构建出 SqlSessionFactory 实例。

    从 XML 文件中构建 SqlSessionFactory 的实例非常简单，建议使用类路径下的资源文件进行配置。 但也可以使用任意的输入流（InputStream）实例，比如用文件路径字符串或 file:// URL 构造的输入流。MyBatis 包含一个名叫 Resources 的工具类，它包含一些实用方法，使得从类路径或其它位置加载资源文件更加容易。

​				sqlSession完全包括了面向数据库执行SQL的所有方法



- 要想有sqlsession首先要有一个工厂，所以以下代码是用来获取sqlsessionFactory的

```java
String resource = "org/mybatis/example/mybatis-config.xml";
InputStream inputStream = Resources.getResourceAsStream(resource);
SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
```

以下代码是获取sqlsession的完整版

```
package com.boshen.utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

//获取sqlsession
public class MybatisUtils {
    private static SqlSessionFactory sqlSessionFactory;
    static{
        try {
            //        加载配置文件中的内容
            String resource = "mybatis-config.xml";
            //        读取用的流对象
            InputStream inputStream = Resources.getResourceAsStream(resource);
//            获取sqlsession的工厂
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//    返回一个sqlSession对象
    public static SqlSession getsqlSession(){
        SqlSession sqlSession = sqlSessionFactory.openSession();
        return sqlSession;
    }
}
```

#### 2.2编写代码

- 实体类

  ```java
  package com.boshen.pojo;
  
  public class User {
      private int id;
      private String name;
      private String pwd;
  
      public User() {
      }
  
      public User(int id, String name, String pwd) {
          this.id = id;
          this.name = name;
          this.pwd = pwd;
      }
  
      public int getId() {
          return id;
      }
  
      public void setId(int id) {
          this.id = id;
      }
  
      public String getName() {
          return name;
      }
  
      public void setName(String name) {
          this.name = name;
      }
  
      public String getPwd() {
          return pwd;
      }
  
      public void setPwd(String pwd) {
          this.pwd = pwd;
      }
  }
  ```

- Dao

  - 在这里规定了要对数据库有哪些操作

- 接口实现类

  - 写mapper.xml用来实现Dao中的接口

  - 这个xml文件是用来给sqlsession进行调用的

    ```xml
    <?xml version="1.0" encoding="UTF8"?>
    <!DOCTYPE mapper
            PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
            "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
    
    <mapper namespace="org.mybatis.example.BlogMapper">
        <select id="selectBlog" resultType="Blog">
            select * from Blog where id = #{id}
        </select>
    </mapper>
    ```


- 注意这里头文件可能有坑，这个是编码集utf8没有-

在以上的代码块中使用namespace和刚才定义的接口进行绑定，之后在mapper标签中写增删改查的语句（相当于实现接口）

id对应实现的哪个方法（相当于实现接口的哪个方法）

resultType相当于返回值（对应到pojo实体类上）

#### 2.3测试

```java
    @Test
    public void test(){
        //获取sqlsession
        SqlSession sqlSession = MybatisUtils.getsqlSession();
//        执行SQL(sqlsession是用来获取接口对象的，有了接口对象之后就可以对应用接口对象的方法做一些事情)
        UserDao mapper = sqlSession.getMapper(UserDao.class);
//        执行接口里面的方法
        List<User> userList = mapper.getUserList();

        for (User user : userList) {
            System.out.println(user);
        }
        sqlSession.close();
        
    }
```

##### 整体小结

- 核心配置文件：需要绑定mapper，需要配置数据库的相关信息
- utils类编写：这个类用来绑定刚才的核心配置文件，用来做一个sqlsession对象，这个对象可以getmapper获得增删改查的操作
- pojo类编写：pojo类是用来接收mybatis的返回值的
- Dao类编写：这里有两个东西，一个是接口，接口定义了一些对数据库操作的方法。还有一个接口的实现mapper。这个mapper用来写sql语句的
- 测试类编写：获得sqlsession，通过sqlsession获得mapper，用mapper执行增删改查的操作，输出结果。

### CRUD（mapper.xml与方法接口）

#### namespace

namespace中的包名要和Dap/mapper一致

#### select

- 查询语句

- id字段：就是对应的接口中的方法名

- resultType：Sql语句执行的返回值

- parameterType：参数类型（例如根据id查询用户的时候，我们需要把id传入进去，这时候这个type应该是一个int类型）

  - 在接口中我们传参，参数在mapper中我们需要使用#{}将其拿出来

  **范例**：

  ```java
  //步骤1
  User getUserById(int id);
  //步骤2
  <select id="getUserById" parameterType="int" resultType="com.boshen.pojo.User">
          select * from user where id =#{id};
  </select>
  //步骤3（测试）
  @Test
      public void testById(){
          SqlSession sqlSession = MybatisUtils.getSqlsession();
          UserDao mapper = sqlSession.getMapper(UserDao.class);
          User u = mapper.getUserById(1);
          System.out.println(u);
          sqlSession.close();
  }
  ```

  我们可以发现使用了mybatis框架之后我们需要修改的东西变的很少了

#### insert

- id：绑定方法名
- parameter：用来接收传入方法中的参数（参数如果是一个对象，那么使用#{}符号可以取出其中的属性）
- 没有返回值
- 注意要提交事务：sqlsession.commit需要写这个语句进行手动的事务提交

#### update

同insert

#### delete

同insert

#### Mybatis大体做的事情

将查询和修改数据库，使用seqsession来获得java编写的具体的sql语句。主配置文件用来连接数据库和连接语句。sqlsession的创建依赖于主配置文件。实体类用来接收查询的返回值，dao用来定义哪些方法和具体的mapper实现这些方法。



### Map作为参数传递（可替代签名任何的数据类型）

在insert语句实现中，我们接口定义的参数需要一个对象，在测试的时候，我们必须新建一个对象，如果这个pojo类有1000000个属性但是我们只想插入其中的部分属性其它都是空，该如何呢？同理我们的update也是如此。这时候我们需要使用map来实现。也就是说传入的参数不再是一个pojo实体类对象，而是一个map。

![image-20220512171108982](C:\Users\admin\AppData\Roaming\Typora\typora-user-images\image-20220512171108982.png)

![image-20220512171234534](C:\Users\admin\AppData\Roaming\Typora\typora-user-images\image-20220512171234534.png)



<img src="C:\Users\admin\AppData\Roaming\Typora\typora-user-images\image-20220512171557593.png" alt="image-20220512171557593" style="zoom:67%;" />

有了这个map的思想，我们可以不管什么都传递map而不用新建对象了。

##### 插入没有name字段的例子

![image-20220512171707247](C:\Users\admin\AppData\Roaming\Typora\typora-user-images\image-20220512171707247.png)



**使用map当作参数其实就是让我们在从parameter中取值的时候数量和名字都自定义即可。**



### 模糊查询

第一步

![image-20220512180023835](C:\Users\admin\AppData\Roaming\Typora\typora-user-images\image-20220512180023835.png)

第二步

![image-20220512180307969](C:\Users\admin\AppData\Roaming\Typora\typora-user-images\image-20220512180307969.png)

第三步：别忘了传递值的时候需要拼接两个%%通配符，但是不能直接在sql上写

![image-20220512180411564](C:\Users\admin\AppData\Roaming\Typora\typora-user-images\image-20220512180411564.png)

这样写死的是不行的，存在sql注入问题，要写以上的版本

![image-20220512180519167](C:\Users\admin\AppData\Roaming\Typora\typora-user-images\image-20220512180519167.png)

代码·实现

```java
第一步    
List<User> searchUserLike(String value);
    
第二步    
<select id="searchUserLike" resultType="com.boshen.pojo.User" parameterType="String">
        select * from user where name like #{value};
</select>
    
第三步
    @Test
    public void testByIdLike(){
        SqlSession sqlSession = MybatisUtils.getSqlsession();
        UserDao mapper = sqlSession.getMapper(UserDao.class);
        List<User> users = mapper.searchUserLike("%f%");
        for (User user : users) {
            System.out.println(user);
        }
        sqlSession.close();
    }
```



## 配置解析

### 核心配置文件mybatis-config.xml

![image-20220512181246655](C:\Users\admin\AppData\Roaming\Typora\typora-user-images\image-20220512181246655.png)

红色箭头部分是重点

### environments（环境变量）

可以适应多个环境但是每一次只能选择一个环境，所谓环境就是数据库连接的哪个，用的是什么数据库

- **环境的切换**

![image-20220512181950788](C:\Users\admin\AppData\Roaming\Typora\typora-user-images\image-20220512181950788.png)

- 其中这里可以配置事务管理器transactionManager和dataSource这两个部分

- 事务管理器有两个，datasource有3个

- 默认的是JDBC事务管理器，pooled的datasource

- **properties属性**

  现在我们属性中的value是写死的，下面将介绍如何使用properties来读取配置文件来替代value中的值

​		第一步：编写数据库配置文件

​		第二步：在核心配置文件中引入

![image-20220512183548005](C:\Users\admin\AppData\Roaming\Typora\typora-user-images\image-20220512183548005.png)

### 别名（typeAliases）

类型别名是为java类型（pojo或者其它对象）设置一个短名字，避免冗余。

<img src="C:\Users\admin\AppData\Roaming\Typora\typora-user-images\image-20220513170535419.png" alt="image-20220513170535419" style="zoom: 80%;" />

在主配置文件中使用typeAliases进行别名映射，加mybatis.config使用这段代码即可将type（java类型对应到一个别名上）

方案1

```xml
<typeAliases>
  <typeAlias alias="Author" type="domain.blog.Author"/>
  <typeAlias alias="Blog" type="domain.blog.Blog"/>
  <typeAlias alias="Comment" type="domain.blog.Comment"/>
  <typeAlias alias="Post" type="domain.blog.Post"/>
  <typeAlias alias="Section" type="domain.blog.Section"/>
  <typeAlias alias="Tag" type="domain.blog.Tag"/>
</typeAliases>
```

方案2，自动扫描包,类名直接变成别名（首字母小写）

```
<typeAliases>
  <package name="domain.blog"/>
</typeAliases>
```

- java自己有的一些数据结构的别名

  例如之前万能的map中提到的，使用map进行传参，Map的别名就是map，HashMap则是hashmap，即大部分的java中自带的类的别名都是讲首字母小写即可

### 设置

完整的设置包含如下所有的设置，其每一个标签代表的含义参考官网。不一定需要每一个在项目中都写出来

```
<settings>
  <setting name="cacheEnabled" value="true"/>
  <setting name="lazyLoadingEnabled" value="true"/>
  <setting name="multipleResultSetsEnabled" value="true"/>
  <setting name="useColumnLabel" value="true"/>
  <setting name="useGeneratedKeys" value="false"/>
  <setting name="autoMappingBehavior" value="PARTIAL"/>
  <setting name="autoMappingUnknownColumnBehavior" value="WARNING"/>
  <setting name="defaultExecutorType" value="SIMPLE"/>
  <setting name="defaultStatementTimeout" value="25"/>
  <setting name="defaultFetchSize" value="100"/>
  <setting name="safeRowBoundsEnabled" value="false"/>
  <setting name="mapUnderscoreToCamelCase" value="false"/>
  <setting name="localCacheScope" value="SESSION"/>
  <setting name="jdbcTypeForNull" value="OTHER"/>
  <setting name="lazyLoadTriggerMethods" value="equals,clone,hashCode,toString"/>
</settings>
```

### mapper映射器

绑定mapper.xml的几种方式

**推荐方式1**

```
方式1：全限定名方式可以直接使用，直接绑定这个mapper.xml
<!-- 使用相对于类路径的资源引用 -->
<mappers>
  <mapper resource="org/mybatis/builder/AuthorMapper.xml"/>
  <mapper resource="org/mybatis/builder/BlogMapper.xml"/>
  <mapper resource="org/mybatis/builder/PostMapper.xml"/>
</mappers>
```

```
方式2：这个方式如果mapper.xml和实现的接口在一个包下即可使用，如若不然则无法找到，并且接口和配置文件必须同名
<!-- 使用映射器接口实现类的完全限定类名 -->
<mappers>
  <mapper class="org.mybatis.builder.AuthorMapper"/>
  <mapper class="org.mybatis.builder.BlogMapper"/>
  <mapper class="org.mybatis.builder.PostMapper"/>
</mappers>
```

```
方式3：直接扫描带有mapper.xml的包
注意：同方式2一样，必须和接口在一起，并且和接口在一个包下
<!-- 将包内的映射器接口实现全部注册为映射器 -->
<mappers>
  <package name="org.mybatis.builder"/>
</mappers>
```



## 生命周期和作用域（sqlsession与其工厂和工厂建造者）

理解我们之前讨论过的不同作用域和生命周期类别是至关重要的，因为错误的使用会导致非常严重的并发问题。

<img src="C:\Users\admin\AppData\Roaming\Typora\typora-user-images\image-20220513174449637.png" alt="image-20220513174449637" style="zoom:50%;" />



sqlsession工厂建造者

- 一旦创建了就不需要它了
- 局部变量

sqlsessionFactory

- 相当于数据库连接池
- **一旦创建没有理由抛弃它**
- 应用作用域（全局），程序开始它就开始，程序结束它就结束
- 使用单例模式，或者静态单例模式

sqlsession

- 相当于数据库连接池连入的请求
- 连接到连接池的一个请求
- 需要关闭，实例不是线程安全，不能被共享
- 放到一个方法离，用完就关闭，否则资源被占用

## ResultMap替代resultType

resultMap是用来接收sql返回的结果集到pojo但是pojo的属性名和sql中的字段名不一致的时候使用。其作用相当于属性resultTupe

一下情况就是属性名和字段名不一致的情况

![image-20220514230402191](C:\Users\admin\AppData\Roaming\Typora\typora-user-images\image-20220514230402191.png)

解决方案

![image-20220514231443215](C:\Users\admin\AppData\Roaming\Typora\typora-user-images\image-20220514231443215.png)

## 日志工厂

报错的时候不会有sql语句的输出，但是我们需要看着这个sql去判断对错。相当于sout和debug

引入日志工厂：在mybatis中具体使用哪个日志实现，在设定中进行设定

![image-20220515213115577](C:\Users\admin\AppData\Roaming\Typora\typora-user-images\image-20220515213115577.png)

<img src="C:\Users\admin\AppData\Roaming\Typora\typora-user-images\image-20220515213510508.png" alt="image-20220515213510508" style="zoom:80%;" />

（setting必须写在properties和typeAliases之间）

STDOUT_LOGGING这个是标准的日志工厂

日志结果

<img src="C:\Users\admin\AppData\Roaming\Typora\typora-user-images\image-20220515214354120.png" alt="image-20220515214354120" style="zoom:67%;" />

## Limit实现分页

为什么要分页（减少初级的处理量）

- 使用Limit分页，sql语法

```
select * form user limit startIndex，pagesize
如果只给定了一个参数，那么就是从0到n
```

- mybatis实现

  - 接口（由于我们需要传递其起始和页大小所以要传两个参数，所以可以使用一个map把这两都放入这个map中）

    ```
    List<User> getUserByLimit(Map<String,Integer> map);
    ```

  - mapper编写

    ```java
        <select id="getUserByLimit" parameterType="map" resultType="user">
            select * from user limit #{startIndex},#{pagesize}
        </select>
    ```

  - 编写测试类

    ```
    @Test
        public void limitSearch(){
            SqlSession sqlSession = MybatisUtils.getSqlsession();
            UserDao mapper = sqlSession.getMapper(UserDao.class);
            Map<String,Integer> map = new HashMap();
            map.put("startIndex",1);
            map.put("pageSize",2);
            List<User> userByLimit = mapper.getUserByLimit(map);
            for (User user : userByLimit) {
                System.out.println(user);
            }
            sqlSession.close();
        }
    ```

    

## 使用注解开发（不常用）

省略写mapper，直接在接口上写一个sql语句当主机即可

例子：

![image-20220515222650042](C:\Users\admin\AppData\Roaming\Typora\typora-user-images\image-20220515222650042.png)

步骤

由于我们使用的是注解，不使用mapper的配置文件了，所以我们需要先将主配置文件中绑定mapper的地方绑定上我们的接口

```
    <mappers>
    //之前我们绑定的是mapper配置文件，现在我们需要绑定接口
<!--        <mapper resource="com/boshen/Dao/mapper.xml"/>-->
        <mapper class="com.boshen.dao.UserDao"/>
    </mappers>
```



## 自动提交事务

目前在我们对表进行修改的时候都是手动的提交事务，即sqlsession.commit去提交事务

这一小结我们需要在创建sqlsession的时候自动将事务提交即在写工具类的时候将事务提交上去

```
public static SqlSession getSqlsession(){
        return sqlSessionFactory.openSession(true);
    }
```

其实就是在使用sqlsession工厂创建sqlsession对象的时候直接将其设置为true即可，这个配置好后我们一切的提交都可以自动完成



## 注解开发@Param注解

这个注解是说明当接口传入多个参数的时候需要使用，其注解中的值就是sql语句中的#{}取出来的参数

![image-20220516180646705](C:\Users\admin\AppData\Roaming\Typora\typora-user-images\image-20220516180646705.png)



## Lombok

通过注解可以补充全get和set方法还有构造方法。

使用步骤：

- 在IDEA中安装Lombok插件

- 在项目中导入lombok的jar包（maven工程直接通过坐标进行引入）

  ```
  <!-- https://mvnrepository.com/artifact/org.projectlombok/lombok -->
  <dependency>
      <groupId>org.projectlombok</groupId>
      <artifactId>lombok</artifactId>
      <version>1.18.24</version>
      <scope>provided</scope>
  </dependency>
  ```

  

- 通过以下注解直接改造pojo类

  ```
  package com.boshen.pojo;
  
  import lombok.AllArgsConstructor;
  import lombok.Data;
  import lombok.NoArgsConstructor;
  
  //data这个注解可以帮助我们生成get，set，tostring，hashcode，equal等方法
  @Data
  //添加有参构造，注意添加完有参构造之后无参构造会自动的消失
  @AllArgsConstructor
  //在这里添加无参构造
  @NoArgsConstructor
  public class User {
      private int id;
      private String name;
      private String possword;
  
  }
  
  ```

## 一对多和多对一在mybatis中的实现

需要用到resultMap中的association（一个复杂类型的关联）和collection（一个复杂类型的集合）相比于下图中提到的resultMap中的result属性（column和property的映射）这个可以实现更加复杂的功能。前者只能注入到字段或者javabean属性的普通结果。

![image-20220514231443215](C:\Users\admin\AppData\Roaming\Typora\typora-user-images\image-20220514231443215.png)





- 案例设计

设计思路：所谓的多对1就是在我们多的一方的实体类中组合进少的一方的实体类的对象。如下所示

```
package com.boshen.pojo;

public class Teacher {
    private int id;
    private String name;
}

```



```
package com.boshen.pojo;

public class Student {
    private int id;
    private String name;
    private Teacher teacher;
}
```

学生类中的每一个学生对象都需要对应一个老师，所以采用组合的方式。



有一个实体类就需要有一个DAO与之对应，一个DAO就需要有一个mapper进行功能的实现

别忘了在主配置文件中把所有的mapper都绑定一遍



- 需求

现在我们有两个表分别是学生表和老师表，我们需要查询学生信息的时候将老师表中对应这个学生的老师的信息也查出来这时候我们需要用到多对一和一对多的相关操作。

#### 方式1按照查询嵌套处理

查询所有的学生信息和对应的老师信息

- 思想：

  java的pojo就是用来接收一个表的实体类，所以一个表中的每一个字段都应该对应到查询出来的表的列名上。而当我们查询出的这个表的列是另外一个表（用连接的方式或者其它方式），这时候我们需要在pojo中再简历另外一个实体类，并将这个实体类与之前的实体类进行组合，组合后原先的pojo就拥有了接收另外一个表的能力。这时候我们需要association这个属性标签来代替result。并且指明这个组合的pojo的类型是什么即这个东西的类名。之后再用另外一个seq语句获得我们想连接的这个表。

- 实现步骤
  - 首先按我们先查找所有的学生信息
    - 由于学生查找后返回的字段中包含老师字段，这不是一个简单的数据类型，而是一个实体类，这时候我们需要将这个实体类（相当于老师的这个表关联到学生表上）
  - 根据查询出来的学生的tid寻找老师

我们可以管这种方式叫做查询嵌套，即当我们需要一个对象作为返回的时候，就单独给这个对象写一个sql把想要的东西放入

```
<?xml version="1.0" encoding="UTF8"?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.boshen.dao.StudentMapper">
<!--    这个关联其实就必须说明跟哪个实体类关联，而哪个实体类就是我们type中要写的值-->
    <resultMap id="studentTeacher" type="com.boshen.pojo.Student">
<!--        这里面property就是类中对应的属性名，column就是我们的表中的列名-->
        <result column="id" property="id"/>
        <result column="name" property="name"/>
        <association property="teacher" column="tid" javaType="com.boshen.pojo.Teacher" select="getTeacherforStudent"/>
    </resultMap>

    <select id="getStudents" resultMap="studentTeacher">
        select * from student;
    </select>

    <select id="getTeacherforStudent" resultType="com.boshen.pojo.Teacher">
        select * from teacher where id = #{id}
    </select>
</mapper>
```

#### 方式2：按照结果嵌套处理

最原始的sql应该是

```sql
select s.id sid,s.name sname,t.name tname from student s,teacher t where s.tid = t.id
```

在第一种方式的时候我们是将这个sql拆成了两个，之后分别用对应到我们的pojo类中。在这种方式中我们不再用这种方式，而是只写这一条sql

```
    <select id="getStudents2" resultMap="mymap2">
        select s.id sid,s.name sname,t.name tname
        from student s,teacher t
        where s.tid = t.id
    </select>
    <resultMap id="mymap2" type="com.boshen.pojo.Student">
        <result property="id" column="sid"/>
        <result property="name" column="sname"/>
        <association property="teacher" javaType="com.boshen.pojo.Teacher">
            <result property="name" column="tname"/>
        </association>
    </resultMap>
```

我们在属性为类的属性上再用一遍result将查询出来的字段，对应到这个类的具体的类名上。从而得到查询结果。





## 一对多在mybatis的处理

一个老师拥有多个学生，对于老师而言就是一对多的关系，老师的pojo类中包含一个学生的list中

在多对1的时候，我们将老师组合进入学生中，那么老师的实体类就是一个老师类，而不包含学生list。在这个实例中学生这个实体类不包含老师类，只包含学生表中的学生信息。在实际应用中，如果两个表有一对多或者多对一的关系的时候，我们需要将其中一个pojo设计包含另外一个pojo的内容。而不需要两个互相包含。

例：如下老师类包含了学生类，并且将其创建成一个表

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Teacher {
    private int id;
    private String name;
    private List<Student> students;
}
```

如下所示，学生类就不能包含老师类了，就把它设计成一个不包含老师信息的一个学生

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student {
    private int id;
    private String name;
}
```



sql在mapper中编写

```xml
    <select id="getTeacherStudent" resultMap="mapTest">
        select s.id sid,s.name sname,t.name tname,t.id tid
        from student s,teacher t
        where s.tid = t.id and t.id = #{tid}
    </select>
    <resultMap type="com.boshen.pojo.Teacher" id="mapTest">
        <result property="id" column="tid"/>
        <result property="name" column="tname"/>
        <collection property="students" ofType="com.boshen.pojo.Student">
            <result property="id" column="sid"/>
            <result property="name" column="sname"/>
        </collection>
    </resultMap>
```

我们可以使用collection标签将对应查询出来的结果中的select字段的所有内容都加载进来。



## 动态sql

**动态sql就是指根据不同的条件生成不同的sql语句**

![image-20220517231350315](C:\Users\admin\AppData\Roaming\Typora\typora-user-images\image-20220517231350315.png)

**动态sql标签（if，choose，trim，foreach）**











