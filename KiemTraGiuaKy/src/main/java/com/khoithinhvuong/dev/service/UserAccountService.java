package com.khoithinhvuong.dev.service;

import com.khoithinhvuong.dev.config.TransactionManager;
import com.khoithinhvuong.dev.model.Student;
import com.khoithinhvuong.dev.model.UserAccount;
import com.khoithinhvuong.dev.repository.StudentRepository;
import com.khoithinhvuong.dev.repository.UserAccountRepository;

import java.time.LocalDate;

public class UserAccountService {

    private final TransactionManager tx;
    private final StudentRepository studentRepository;
    private final UserAccountRepository userAccountRepository;

    public UserAccountService(TransactionManager tx,
                              StudentRepository studentRepository,
                              UserAccountRepository userAccountRepository) {
        this.tx = tx;
        this.studentRepository = studentRepository;
        this.userAccountRepository = userAccountRepository;
    }
    public UserAccount getUserByUserName(String username)
    {
        return userAccountRepository.findByUserName(username);
    }
    public void createStudentWithAccount(
            String fullName,
            LocalDate dateOfBirth,
            String gender,
            String phone,
            String email,
            String address,
            LocalDate registrationDate,
            String status,
            String username,
            String passwordHash
    ) {

        tx.runInTransaction(em -> {

            // 1️⃣ Tạo Student
            Student student = new Student(
                    null,
                    fullName,
                    dateOfBirth,
                    gender,
                    phone,
                    email,
                    address,
                    registrationDate,
                    status
            );

            studentRepository.create(student);

            // ID đã có sau persist
            Long generatedId = student.getStudentId();

            // 2️⃣ Tạo Account
            UserAccount account = new UserAccount(
                    null,
                    username,
                    passwordHash,
                    UserAccount.Role.STUDENT,
                    generatedId
            );

            userAccountRepository.create(account);

            return null;
        });
    }
}
