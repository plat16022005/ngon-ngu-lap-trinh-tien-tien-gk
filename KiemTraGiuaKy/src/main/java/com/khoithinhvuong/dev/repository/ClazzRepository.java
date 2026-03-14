package com.khoithinhvuong.dev.repository;

import com.khoithinhvuong.dev.model.Clazz;

import java.util.List;

public interface ClazzRepository {
    void create(Clazz clazz);
    void update(Clazz clazz);
    Clazz findById(Long id);
    List<Clazz> findAll();
    void delete (Long id);
    // Lọc lớp học theo giáo viên (Sử dụng cho Role Teacher)
    List<Clazz> findByTeacher(Long teacherId);
    List<Clazz> findByCourse(Long courseId);
    List<Clazz> findByStudent(Long studentId);
}
