package com.khoithinhvuong.dev.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.Check;
import java.time.LocalDate;

@Entity
@Table(name = "payment")
@Check(constraints = "amount > 0")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @NotNull
    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private LocalDate paymentDate;

    @NotBlank
    @Column(nullable = false, length = 50)
    private String paymentMethod;

    @NotBlank
    @Column(nullable = false, length = 20)
    private String status;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @OneToOne
    @JoinColumn(name = "enrollment_id", nullable = false, unique = true)
    private Enrollment enrollment;
}
