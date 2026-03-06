package com.khoithinhvuong.dev.service;

import com.khoithinhvuong.dev.config.TransactionManager;
import com.khoithinhvuong.dev.model.Student;
import com.khoithinhvuong.dev.model.UserAccount;
import com.khoithinhvuong.dev.repository.StudentRepository;
import com.khoithinhvuong.dev.repository.UserAccountRepository;
import com.khoithinhvuong.dev.repository.jpa.JpaUserAccountRepository;

import java.time.LocalDate;

public class UserAccountService {
    private static TransactionManager tx = new TransactionManager();
    private UserAccountRepository userAccountRepository = new JpaUserAccountRepository(tx);

    public UserAccount getUserByUserName(String username)
    {
        return userAccountRepository.findByUserName(username);
    }
    public UserAccount CreateUserAccount(
            String username,
            String passwordHash,
            Long generatedId
    )
    {
        UserAccount account = new UserAccount(
                null,
                username,
                passwordHash,
                UserAccount.Role.STUDENT,
                generatedId
        );

        userAccountRepository.create(account);
        return account;
    }
//    public void createStudentWithAccount(
//            String fullName,
//            LocalDate dateOfBirth,
//            String gender,
//            String phone,
//            String email,
//            String address,
//            LocalDate registrationDate,
//            String status,
//            String username,
//            String passwordHash
//    )
//    {
//
//        tx.runInTransaction(em -> {
//
//            // 1️⃣ Tạo Student
//            Student student = new Student(
//                    null,
//                    fullName,
//                    dateOfBirth,
//                    gender,
//                    phone,
//                    email,
//                    address,
//                    registrationDate,
//                    status
//            );
//
//            studentRepository.create(student);
//
//            // ID đã có sau persist
//            Long generatedId = student.getStudentId();
//
//            // 2️⃣ Tạo Account
//            UserAccount account = new UserAccount(
//                    null,
//                    username,
//                    passwordHash,
//                    UserAccount.Role.STUDENT,
//                    generatedId
//            );
//
//            userAccountRepository.create(account);
//
//            return null;
//        });
//    }
}
