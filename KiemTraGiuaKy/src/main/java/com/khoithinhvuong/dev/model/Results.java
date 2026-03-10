package com.khoithinhvuong.dev.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

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
    private Clazz clazzEntity;

    public Results() {}

    public Results(Long resultId, Double score, String grade, String comment, Student student, Clazz clazzEntity) {
        this.resultId = resultId;
        this.score = score;
        this.grade = grade;
        this.comment = comment;
        this.student = student;
        this.clazzEntity = clazzEntity;
    }

    public Long getResultId() {
        return resultId;
    }

    public void setResultId(Long resultId) {
        this.resultId = resultId;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Clazz getClassEntity() {
        return clazzEntity;
    }

    public void setClassEntity(Clazz clazzEntity) {
        this.clazzEntity = clazzEntity;
    }
}
