package com.khoithinhvuong.dev.repository;

import com.khoithinhvuong.dev.model.Student;

import java.util.List;

public interface StudentRepository {
    public void create(Student student);

    Student findByEmail(String email);
    List<Student> findByFullNameContainingIgnoreCase(String fullName);
    void update(Student student);
    Student findById(Long studentId);
    List<Student> findAll();
    void delete(Long studentId);

}
