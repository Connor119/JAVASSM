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

#### insert（看到14.50明天继续）

- id：绑定方法名
- parameter：用来接收传入方法中的参数（参数如果是一个对象，那么使用#{}符号可以取出其中的属性）
- 没有返回值
- 注意要提交事务：sqlsession.commit需要写这个语句进行手动的事务提交
