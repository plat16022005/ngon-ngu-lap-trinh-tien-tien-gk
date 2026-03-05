package com.khoithinhvuong.dev.repository.jpa;

import com.khoithinhvuong.dev.config.TransactionManager;
import com.khoithinhvuong.dev.model.Student;
import com.khoithinhvuong.dev.repository.StudentRepository;

public class JpaStudentRepository implements StudentRepository {
    private final TransactionManager tx;
    public JpaStudentRepository(TransactionManager tx)
    {
        this.tx = tx;
    }
    public void create(Student student)
    {
        tx.runInTransaction(em -> {
           em.persist(student);
           return null;
        });
    }
}
