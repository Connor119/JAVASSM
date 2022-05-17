package com.boshen.dao;

import com.boshen.pojo.Teacher;

public interface TeacherMapper {
    Teacher getTeacher(int id);
    Teacher getTeacherStudent(int id);
}
