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
  - 编写mybatis的工具类









