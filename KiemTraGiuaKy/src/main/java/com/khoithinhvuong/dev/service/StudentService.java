package com.khoithinhvuong.dev.service;

import com.khoithinhvuong.dev.config.TransactionManager;
import com.khoithinhvuong.dev.model.Student;
import com.khoithinhvuong.dev.repository.StudentRepository;
import com.khoithinhvuong.dev.repository.jpa.JpaStudentRepository;

import java.time.LocalDate;

public class StudentService {
    private static TransactionManager tx = new TransactionManager();
    private StudentRepository studentRepository = new JpaStudentRepository(tx);
    public Student CreateStudent(String address,
                              LocalDate dateOfBirth,
                              String email,
                              String fullName,
                              String gender,
                              String phone,
                              LocalDate registrationDate,
                              String status)
    {
        Student student = new Student(null, fullName, dateOfBirth, gender, phone, email, address, registrationDate, status);
        studentRepository.create(student);
        return student;
    }
}
