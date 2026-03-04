package com.khoithinhvuong.dev.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Entity
@Table(name = "teacher")
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teacherId;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String fullName;

    @Column(length = 15)
    private String phone;

    @Email
    @NotBlank
    @Column(nullable = false, unique = true)
    private String email;

    @Column(length = 100)
    private String specialty;

    @PastOrPresent
    @Column(nullable = false)
    private LocalDate hireDate;

    @NotBlank
    @Column(nullable = false, length = 20)
    private String status;
}
