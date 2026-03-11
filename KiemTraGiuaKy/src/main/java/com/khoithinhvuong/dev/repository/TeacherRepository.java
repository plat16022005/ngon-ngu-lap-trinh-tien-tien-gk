package com.khoithinhvuong.dev.repository;

import com.khoithinhvuong.dev.model.Teacher;

import java.util.List;

public interface TeacherRepository {
    void create(Teacher teacher);
    void update(Teacher teacher);
    List<Teacher> findAll();
    Teacher findById(Long teacherId);
    void delete(Long teacherId);

}
