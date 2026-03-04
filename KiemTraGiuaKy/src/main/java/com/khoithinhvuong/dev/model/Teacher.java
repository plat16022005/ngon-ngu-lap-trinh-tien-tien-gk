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

    public Teacher() {}

    public Teacher(Long teacherId, String fullName, String phone, String email, String specialty, LocalDate hireDate, String status) {
        this.teacherId = teacherId;
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
        this.specialty = specialty;
        this.hireDate = hireDate;
        this.status = status;
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
