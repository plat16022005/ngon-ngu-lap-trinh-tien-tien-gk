package com.khoithinhvuong.dev.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Entity
@Table(name = "student")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studentId;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String fullName;

    @Past
    private LocalDate dateOfBirth;

    @NotBlank
    @Column(length = 10)
    private String gender;

    @Column(length = 15)
    private String phone;

    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @Column(length = 255)
    private String address;

    @Column(nullable = false)
    private LocalDate registrationDate;

    @NotBlank
    @Column(nullable = false, length = 20)
    private String status;

}
