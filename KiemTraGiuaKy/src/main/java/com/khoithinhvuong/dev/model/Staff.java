package com.khoithinhvuong.dev.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import javax.annotation.processing.Generated;

@Entity
@Table(name = "staff")
public class Staff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long staffId;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String fullName;

    @NotBlank
    @Size(max = 50)
    @Column(nullable = false, length = 50)
    private String role;

    @Column(length = 15)
    private String phone;

    @Email
    @Column(nullable = false, unique = true)
    private String email;

    //constructor
    public Staff() {
    }

    public Staff(Long staffId, String fullName, String role, String phone, String email) {
        this.staffId = staffId;
        this.fullName = fullName;
        this.role = role;
        this.phone = phone;
        this.email = email;
    }

    //Getter & Setter

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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
}
