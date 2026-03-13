package com.khoithinhvuong.dev.repository.jpa;

import com.khoithinhvuong.dev.config.TransactionManager;
import com.khoithinhvuong.dev.model.Payment;
import com.khoithinhvuong.dev.repository.PaymentRepository;

import java.util.List;

public class JpaPaymentRepository implements PaymentRepository {
    private final TransactionManager tx;

    public JpaPaymentRepository(TransactionManager tx) {
        this.tx = tx;
    }

    @Override
    public void create(Payment payment) {
        tx.runInTransaction(em -> {
            em.persist(payment);
            return null;
        });
    }

    @Override
    public void update(Payment payment) {
        tx.runInTransaction(em -> {
            em.merge(payment);
            return null;
        });
    }

    @Override
    public Payment findById(Long paymentId) {
        return tx.runInTransaction(em ->
                em.find(Payment.class, paymentId)
        );
    }

    @Override
    public List<Payment> findAll() {
        return tx.runInTransaction(em ->
                em.createQuery("FROM Payment", Payment.class)
                        .getResultList()
        );
    }

    @Override
    public void delete(Long paymentId) {
        tx.runInTransaction(em -> {
            Payment payment = em.find(Payment.class, paymentId);
            if (payment != null) {
                em.remove(payment);
            }
            return null;
        });
    }
}
