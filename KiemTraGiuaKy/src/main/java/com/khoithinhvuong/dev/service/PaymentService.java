package com.khoithinhvuong.dev.service;

import com.khoithinhvuong.dev.config.TransactionManager;
import com.khoithinhvuong.dev.model.Payment;
import com.khoithinhvuong.dev.repository.PaymentRepository;
import com.khoithinhvuong.dev.repository.jpa.JpaPaymentRepository;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

public class PaymentService {

    private static final TransactionManager tx = new TransactionManager();
    private final PaymentRepository paymentRepository =
            new JpaPaymentRepository(tx);


    public void createPayment(Payment payment) {

        boolean alreadyPaid = paymentRepository.findAll()
                .stream()
                .anyMatch(p -> p.getEnrollment().getEnrollmentId()
                        .equals(payment.getEnrollment().getEnrollmentId()));

        if (alreadyPaid) {
            throw new RuntimeException("Enrollment này đã thanh toán!");
        }

        payment.setPaymentDate(LocalDate.now());
        paymentRepository.create(payment);
    }


    public List<Payment> getPaymentsByStudent(Long studentId) {

        return paymentRepository.findAll()
                .stream()
                .filter(p -> p.getStudent().getStudentId().equals(studentId))
                .toList();
    }


    public List<Payment> getAllPaymentsSorted() {

        return paymentRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Payment::getPaymentDate))
                .toList();
    }

    public Payment getPaymentByEnrollment(Long enrollmentId) {

        return paymentRepository.findAll()
                .stream()
                .filter(p -> p.getEnrollment().getEnrollmentId().equals(enrollmentId))
                .findFirst()
                .orElse(null);
    }


    public void deletePaymentsByStudent(Long studentId) {

        paymentRepository.findAll()
                .stream()
                .filter(p -> p.getStudent().getStudentId().equals(studentId))
                .forEach(p -> paymentRepository.delete(p.getPaymentId()));
    }
}