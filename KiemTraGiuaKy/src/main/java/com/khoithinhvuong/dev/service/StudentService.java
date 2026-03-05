package com.khoithinhvuong.dev.service;

import com.khoithinhvuong.dev.model.Student;
import com.khoithinhvuong.dev.repository.StudentRepository;

import java.time.LocalDate;

public class StudentService {
    private final StudentRepository studentRepository;
    public StudentService(StudentRepository studentRepository)
    {
        this.studentRepository = studentRepository;
    }
    public void CreateStudent(String address,
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
    }
}
