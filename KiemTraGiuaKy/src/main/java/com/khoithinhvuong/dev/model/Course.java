package com.khoithinhvuong.dev.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "course")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseId;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String courseName;

    @Column(length = 500)
    private String description;

    @NotBlank
    @Column(nullable = false, length = 50)
    private String level;

    @NotNull
    @Min(1)
    @Column(nullable = false)
    private Integer duration; // số tuần / tháng tùy bạn

    @NotNull
    @Min(0)
    @Column(nullable = false)
    private Double fee;

    @NotBlank
    @Column(nullable = false, length = 20)
    private String status;

}
