package com.khoithinhvuong.dev.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "room")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;

    @NotBlank
    @Column(nullable = false, length = 50)
    private String roomName;

    @NotNull
    @Min(1)
    @Column(nullable = false)
    private Integer capacity;

    @Column(length = 100)
    private String location;

    @NotBlank
    @Column(nullable = false, length = 20)
    private String status;
}
