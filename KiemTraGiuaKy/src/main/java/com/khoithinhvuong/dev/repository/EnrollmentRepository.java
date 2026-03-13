package com.khoithinhvuong.dev.repository;

import com.khoithinhvuong.dev.model.Enrollment;

import java.util.List;

public interface EnrollmentRepository {
    void create(Enrollment enrollment);

    void update(Enrollment enrollment);

    Enrollment findById(Long enrollmentId);

    List<Enrollment> findAll();

    List<Enrollment> findByStudentId(Long studentId);

    void delete(Long enrollmentId);

    boolean checkAlreadyRegistered(Long studentId, Long classId);

    Long countStudentByClass(Long ClassId );

    void updateState(Long enrolmentId, String state);
}
