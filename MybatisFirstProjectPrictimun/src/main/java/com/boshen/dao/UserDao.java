package com.boshen.dao;

import com.boshen.pojo.User;

import java.util.List;
import java.util.Map;

public interface UserDao {
    List<User> getUserList();
    User getUserById(int id);
    void insertUser(User user);
    void insertUser2(Map<String,Object> map);

    List<User> searchUserLike(String value);

//    分页
    List<User> getUserByLimit(Map<String,Integer> map);

//    @Select("select * from user")
//    List<User> getUserListAnnotation();


}
