package com.boshen.dao;

import com.boshen.pojo.Student;
import com.boshen.pojo.Teacher;
import com.boshen.pojo.User;
import com.boshen.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Test
    public void testinsert(){
        SqlSession sqlSession = MybatisUtils.getSqlsession();
        UserDao mapper = sqlSession.getMapper(UserDao.class);
        mapper.insertUser(new User(4,"fbs","123"));
        sqlSession.commit();
        sqlSession.close();
    }


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

    @Test
    public void testinsert2(){
        SqlSession sqlSession = MybatisUtils.getSqlsession();
        UserDao mapper = sqlSession.getMapper(UserDao.class);
        Map<String,Object> userNoPWD = new HashMap();
        userNoPWD.put("mapId",6);
        userNoPWD.put("mapName","yxy");
        mapper.insertUser2(userNoPWD);
        sqlSession.commit();
        sqlSession.close();
    }
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

    @Test
    public void testTeacherGet(){
        SqlSession sqlSession = MybatisUtils.getSqlsession();
        TeacherMapper mapper = sqlSession.getMapper(TeacherMapper.class);
        Teacher teacher = mapper.getTeacher(1);
        System.out.println(teacher);
        sqlSession.close();
    }


    @Test
    public void testStudentTeacher(){
        SqlSession sqlSession = MybatisUtils.getSqlsession();
        StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
        List<Student> students = mapper.getStudents();
        for (Student student : students) {
            System.out.println(student);
        }
        sqlSession.close();
    }
    @Test
    public void testStudentTeacher2(){
        SqlSession sqlSession = MybatisUtils.getSqlsession();
        StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
        List<Student> students = mapper.getStudents2();
        for (Student student : students) {
            System.out.println(student);
        }
        sqlSession.close();
    }


    @Test
    public void testStudentTeacher3(){
        SqlSession sqlSession = MybatisUtils.getSqlsession();
        TeacherMapper mapper = sqlSession.getMapper(TeacherMapper.class);
        Teacher teacherStudent = mapper.getTeacherStudent(1);
        System.out.println(teacherStudent);
        sqlSession.close();
    }


//    @Test
//    public void annotationSearch(){
//        //        获取sqlsession
//        SqlSession sqlSession = MybatisUtils.getSqlsession();
////        使用sqlsession获取接口，接口由mapper.xml实现，这时候我们可以使用接口中定义的方法了
//        UserDao mapper = sqlSession.getMapper(UserDao.class);
//        List<User> userList = mapper.getUserListAnnotation();
//
//        for (User user : userList) {
//            System.out.println(user);
//        }
//        sqlSession.close();
//    }


}
