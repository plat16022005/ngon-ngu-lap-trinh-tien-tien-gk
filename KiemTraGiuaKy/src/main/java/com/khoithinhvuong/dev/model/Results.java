package com.khoithinhvuong.dev.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "result")
public class Results {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long resultId;

    @NotNull
    @Column(nullable = false)
    private Double score;

    @Column(length = 5)
    private String grade;

    @Column(length = 255)
    private String comment;

    @OneToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "class_id", nullable = false)
    private Clasz classEntity;
}
