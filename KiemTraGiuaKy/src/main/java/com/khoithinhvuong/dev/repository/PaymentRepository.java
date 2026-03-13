package com.khoithinhvuong.dev.repository;

import com.khoithinhvuong.dev.model.Payment;

import java.util.List;

public interface PaymentRepository {
    void create(Payment payment);

    void update(Payment payment);

    Payment findById(Long paymentId);

    List<Payment> findAll();

    void delete(Long paymentId);


}
