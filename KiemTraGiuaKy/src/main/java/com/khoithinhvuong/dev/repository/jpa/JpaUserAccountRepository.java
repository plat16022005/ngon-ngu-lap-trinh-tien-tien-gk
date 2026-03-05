package com.khoithinhvuong.dev.repository.jpa;

import com.khoithinhvuong.dev.config.TransactionManager;
import com.khoithinhvuong.dev.model.UserAccount;
import com.khoithinhvuong.dev.repository.UserAccountRepository;

import jakarta.persistence.NoResultException;

public class JpaUserAccountRepository implements UserAccountRepository {

    private final TransactionManager tx;

    public JpaUserAccountRepository(TransactionManager tx) {
        this.tx = tx;
    }

    @Override
    public UserAccount findByUserName(String username) {
        return tx.runInTransaction(em ->
            em.createQuery(
                    "FROM UserAccount u WHERE u.username = :username",
                    UserAccount.class
            )
            .setParameter("username", username)
            .getResultStream()
            .findFirst()
            .orElse(null)
        );
    }

    @Override
    public void create(UserAccount userAccount)
    {
        tx.runInTransaction(em -> {
            em.persist(userAccount);
            return null;
        });
    }
}