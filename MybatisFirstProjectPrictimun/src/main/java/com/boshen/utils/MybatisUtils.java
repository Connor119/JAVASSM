package com.boshen.utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class MybatisUtils {
    /*在这里我们需要绑定我们的核心配置文件，
    而核心配置文件中绑定了我们的mapper，
    这就给执行后序接口中的方法进行了连接*/
//    我们需要通过建造者模式获得工厂的实例化对象，需要通过这个工厂来获得我们需要的东西
    static SqlSessionFactory sqlSessionFactory;
    static{

        try {
            //        获得核心配置文件的路径
            String resource = "Mybatis-config.xml";
//        用流对象进行读取
            InputStream inputStream = Resources.getResourceAsStream(resource);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//    可以使用这个方法获得sqlsession实例化对象
    public static SqlSession getSqlsession(){
        return sqlSessionFactory.openSession(true);
    }
}
