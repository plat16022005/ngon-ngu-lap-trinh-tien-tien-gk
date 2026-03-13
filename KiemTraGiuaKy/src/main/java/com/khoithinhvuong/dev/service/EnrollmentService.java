package com.khoithinhvuong.dev.service;

import com.khoithinhvuong.dev.config.TransactionManager;
import com.khoithinhvuong.dev.model.Enrollment;
import com.khoithinhvuong.dev.repository.EnrollmentRepository;
import com.khoithinhvuong.dev.repository.jpa.JpaEnrollmentRepository;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

public class EnrollmentService {
    private static final TransactionManager tx = new TransactionManager();
    private final EnrollmentRepository enrollmentRepository =
            new JpaEnrollmentRepository(tx);

    public void register(Enrollment enrollment) {

        // 1. Lấy ID của học sinh và lớp học từ đối tượng truyền vào
        Long studentId = enrollment.getStudent().getStudentId();
        Long classId = enrollment.getClassId();

        // 2. Gọi thẳng xuống DB để nhờ DB kiểm tra giùm
        boolean alreadyRegistered = enrollmentRepository.checkAlreadyRegistered(studentId, classId);

        // 3. Xử lý kết quả
        if (alreadyRegistered) {
            throw new RuntimeException("Sinh viên đã đăng ký lớp học này rồi!");
        }

        enrollment.setEnrollmentDate(LocalDate.now());
        enrollmentRepository.create(enrollment);
    }

    public List<Enrollment> getEnrollmentsByStudent(Long studentId) {

        return enrollmentRepository.findAll()
                .stream()
                .filter(e -> e.getStudent().getStudentId().equals(studentId))
                .toList();
    }

    public List<Enrollment> getAllEnrollmentsSorted() {

        return enrollmentRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Enrollment::getEnrollmentDate))
                .toList();
    }

    public Enrollment getEnrollmentByStudent(Long studentId) {

        return enrollmentRepository.findAll()
                .stream()
                .filter(e -> e.getStudent().getStudentId().equals(studentId))
                .findFirst()
                .orElse(null);
    }

    public Long countAccurateStudent(Long ClassId){
        return enrollmentRepository.countStudentByClass(ClassId);
    }

    public void deleteEnrollmentsByStudent(Long studentId) {

        enrollmentRepository.findAll()
                .stream()
                .filter(e -> e.getStudent().getStudentId().equals(studentId))
                .forEach(e -> enrollmentRepository.delete(e.getEnrollmentId()));
    }
    public void updateState(Long enrollmentId, String state){
        enrollmentRepository.updateState(enrollmentId,state);
    }
}
