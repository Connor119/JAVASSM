package com.boshen.Dao;

import com.boshen.pojo.User;

import java.util.List;

public interface UserDao {
    List<User> getUserList();
    User getUserById(int id);
}
