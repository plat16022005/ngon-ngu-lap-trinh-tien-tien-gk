package com.khoithinhvuong.dev.service;

import com.khoithinhvuong.dev.config.TransactionManager;
import com.khoithinhvuong.dev.model.Teacher;
import com.khoithinhvuong.dev.repository.TeacherRepository;
import com.khoithinhvuong.dev.repository.jpa.JpaTeacherRepository;

import java.util.List;

public class TeacherService {
    private static final TransactionManager tx = new TransactionManager();
    private final TeacherRepository teacherRepository = new JpaTeacherRepository(tx);

    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    public Teacher getTeacherById(Long id) {
        return teacherRepository.findById(id);
    }
}
