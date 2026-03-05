package com.khoithinhvuong.dev;

import javax.swing.*;
import com.khoithinhvuong.dev.config.JpaUtil;
import com.khoithinhvuong.dev.config.TransactionManager;
import com.khoithinhvuong.dev.model.UserAccount;
import com.khoithinhvuong.dev.repository.StudentRepository;
import com.khoithinhvuong.dev.repository.UserAccountRepository;
import com.khoithinhvuong.dev.repository.jpa.JpaStudentRepository;
import com.khoithinhvuong.dev.repository.jpa.JpaUserAccountRepository;
import com.khoithinhvuong.dev.service.UserAccountService;
import jakarta.persistence.EntityManager;

import java.io.Console;
import java.time.LocalDate;

public class MainFrame extends JFrame {

    public MainFrame() {
        setTitle("Java Swing Demo");
        setSize(400, 300);
        setLocationRelativeTo(null); // căn giữa màn hình
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel label = new JLabel("Hello Java Swing!", SwingConstants.CENTER);
        add(label);
    }
    private UserAccountService userAccountService;
    public static void main(String[] args) {

        // 1️⃣ Tạo TransactionManager
        TransactionManager tx = new TransactionManager();

        // 2️⃣ Tạo Repository
        UserAccountRepository repo =
                new JpaUserAccountRepository(tx);
        StudentRepository studentRepo = new JpaStudentRepository(tx);

        // 3️⃣ Tạo Service
        UserAccountService service =
                new UserAccountService(tx, studentRepo, repo);
        service.createStudentWithAccount("Pham Le Anh Tuan",
                LocalDate.of(2005,2,16),
                "male",
                "0933577076",
                "phamleanhtuan16022005@gmail.com",
                "Tay Ninh",
                LocalDate.now(),
                "Active",
                "anhtuan",
                "dqwjwqiudjwqdwqdw");
        // 4️⃣ Test DB
        UserAccount user = service.getUserByUserName("anhtuan");

        if (user != null) {
            System.out.println("User ID: " + user.getUserId());
        } else {
            System.out.println("User not found");
        }

        // 5️⃣ Mở UI
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}