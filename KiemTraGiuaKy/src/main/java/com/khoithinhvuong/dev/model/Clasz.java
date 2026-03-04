package com.khoithinhvuong.dev.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.Check;
import java.time.LocalDate;

@Entity
@Table(name = "class")
@Check(constraints = "end_date >= start_date")
public class Clasz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long classId;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String className;

    @NotNull
    @Column(nullable = false)
    private LocalDate startDate;

    @NotNull
    @Column(nullable = false)
    private LocalDate endDate;

    @NotNull
    @Min(1)
    @Column(nullable = false)
    private Integer maxStudent;

    @NotBlank
    @Column(nullable = false, length = 20)
    private String status;

    @ManyToOne
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    @ManyToOne
    @Column(name = "course_id", nullable = false)
    private Long courseId;
}
