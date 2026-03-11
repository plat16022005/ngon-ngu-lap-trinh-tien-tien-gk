package com.khoithinhvuong.dev.service;

import com.khoithinhvuong.dev.config.TransactionManager;
import com.khoithinhvuong.dev.model.Clazz;
import com.khoithinhvuong.dev.model.Course;
import com.khoithinhvuong.dev.model.Teacher;
import com.khoithinhvuong.dev.repository.ClazzRepository;
import com.khoithinhvuong.dev.repository.jpa.JpaClazzRepository;

import java.time.LocalDate;
import java.util.List;

public class ClazzService
{
    private static final TransactionManager tx = new TransactionManager();
    private final ClazzRepository clazzRepository = new JpaClazzRepository(tx);

    public void createClass(String name, LocalDate start, LocalDate end, Integer max, Teacher teacher, Course course) {
        Clazz clazz = new Clazz(null, name, start, end, max, "Open", teacher, course);
        clazzRepository.create(clazz);
    }
    public List<Clazz> getAllClasses() {
        return clazzRepository.findAll();
    }

    public List<Clazz> getClassesByTeacher(Long teacherId) {
        return clazzRepository.findByTeacher(teacherId);
    }
}