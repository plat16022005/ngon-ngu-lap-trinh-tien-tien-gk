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

    public Course() {}

    public Course(Long courseId, String courseName, String description, String level, Integer duration, Double fee, String status) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.description = description;
        this.level = level;
        this.duration = duration;
        this.fee = fee;
        this.status = status;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
