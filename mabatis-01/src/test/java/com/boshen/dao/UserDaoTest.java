package com.boshen.dao;

import com.boshen.pojo.User;
import com.boshen.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.List;

public class UserDaoTest {
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
}
