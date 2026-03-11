package com.khoithinhvuong.dev.service;

import com.khoithinhvuong.dev.config.TransactionManager;
import com.khoithinhvuong.dev.model.Teacher;
import com.khoithinhvuong.dev.repository.TeacherRepository;
import com.khoithinhvuong.dev.repository.jpa.JpaTeacherRepository;

import java.time.LocalDate;
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

    public void createTeacher(String name, String phone, String email, String specialty, LocalDate hireDate)
    {
        Teacher teacher = new Teacher(null, name, phone, email, specialty, hireDate, "Active");
        teacherRepository.create(teacher);
    }

    public void updateTeacher(Teacher teacher) {
        teacherRepository.update(teacher);
    }

    public void deleteTeacher(Long id) {
        teacherRepository.delete(id);
    }

    //Tìm kiếm giáo viên theo chuyên môn
    public List<Teacher> searchBySpecialty(String specialty) {
        return teacherRepository.findAll().stream()
                .filter(t -> t.getSpecialty().toLowerCase().contains(specialty.toLowerCase()))
                .toList();
    }
}
