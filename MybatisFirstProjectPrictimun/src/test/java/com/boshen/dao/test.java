package com.boshen.dao;

import com.boshen.Dao.UserDao;
import com.boshen.pojo.User;
import com.boshen.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.List;

public class test {
    @Test
    public void test(){
//        获取sqlsession
        SqlSession sqlSession = MybatisUtils.getSqlsession();
//        使用sqlsession获取接口，接口由mapper.xml实现，这时候我们可以使用接口中定义的方法了
        UserDao mapper = sqlSession.getMapper(UserDao.class);
        List<User> userList = mapper.getUserList();

        for (User user : userList) {
            System.out.println(user);
        }
        sqlSession.close();
    }
    @Test
    public void testById(){
        SqlSession sqlSession = MybatisUtils.getSqlsession();
        UserDao mapper = sqlSession.getMapper(UserDao.class);
        User u = mapper.getUserById(1);
        System.out.println(u);
        sqlSession.close();
    }
}
