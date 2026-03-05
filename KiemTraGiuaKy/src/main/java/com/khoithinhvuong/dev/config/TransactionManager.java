package com.khoithinhvuong.dev.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class TransactionManager {

    public <T> T runInTransaction(JpaWork<T> work) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            T result = work.execute(em);
            tx.commit();
            return result;
        } catch (Exception ex) {
            if (tx.isActive()) tx.rollback();
            throw new RuntimeException(ex);
        } finally {
            em.close();
        }
    }
}
