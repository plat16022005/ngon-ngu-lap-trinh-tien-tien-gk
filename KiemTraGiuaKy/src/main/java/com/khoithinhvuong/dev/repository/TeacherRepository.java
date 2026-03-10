package com.khoithinhvuong.dev.repository;

import com.khoithinhvuong.dev.model.Teacher;

import java.util.List;

public interface TeacherRepository {
    void save(Teacher teacher);
    List<Teacher> findAll();
    Teacher findById(long teacherId);
    void delete(long teacherId);

}
