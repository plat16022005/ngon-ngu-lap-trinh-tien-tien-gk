package com.khoithinhvuong.dev.service;

import com.khoithinhvuong.dev.config.PasswordUtils;
import com.khoithinhvuong.dev.config.TransactionManager;
import com.khoithinhvuong.dev.model.Student;
import com.khoithinhvuong.dev.model.UserAccount;

import java.time.LocalDate;

public class AuthService {
    private StudentService studentService = new StudentService();
    private UserAccountService userAccountService = new UserAccountService();
    private TransactionManager tx = new TransactionManager();

    public Boolean login(String username, String password)
    {
        UserAccount account = userAccountService.getUserByUserName(username);
        if (account == null)
        {
            System.out.println("Ko thấy tài khoản");
            return false;
        }
        if (!account.getPasswordHash().equals(PasswordUtils.hashPassword(password)))
        {
            System.out.println("Mật khẩu ko đúng");
            return false;
        }
        return true;
    }
    public Boolean register(
            String fullName,
            LocalDate dateOfBirth,
            String gender,
            String phone,
            String email,
            String address,
            LocalDate registrationDate,
            String status,
            String username,
            String password,
            String rePassword
    ) {
        if (!password.equals(rePassword))
        {
            System.out.println("Nhập lại mật khẩu");
            return false;
        }
        if (userAccountService.getUserByUserName(username) != null)
        {
            System.out.println("Có tài khoản này rồi!");
            return false;
        }
        tx.runInTransaction(em -> {

            Student student = studentService.CreateStudent(address,dateOfBirth,email,fullName,gender,phone,registrationDate,status);

            Long studentId = student.getStudentId();

            UserAccount account = userAccountService.CreateUserAccount(username, PasswordUtils.hashPassword(password), studentId);

            return null;
        });
        return true;
    }
}
