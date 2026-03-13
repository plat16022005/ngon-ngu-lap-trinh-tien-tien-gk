package com.khoithinhvuong.dev.repository.jpa;

import com.khoithinhvuong.dev.config.TransactionManager;
import com.khoithinhvuong.dev.model.Student;
import com.khoithinhvuong.dev.repository.StudentRepository;

import java.util.List;

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

    @Override
    public Student findByEmail(String email) {
        return tx.runInTransaction(em -> {
            List<Student> result = em.createQuery(
                            "SELECT s FROM Student s WHERE s.email = :email",
                            Student.class)
                    .setParameter("email", email)
                    .getResultList();

            return result.isEmpty() ? null : result.get(0);
        });
    }

    @Override
    public List<Student> findByFullNameContainingIgnoreCase(String fullName) {
        return tx.runInTransaction(em ->
                em.createQuery(
                                "SELECT s FROM Student s WHERE LOWER(s.fullName) LIKE LOWER(:name)",
                                Student.class)
                        .setParameter("name", "%" + fullName + "%")
                        .getResultList()
        );
    }

    @Override
    public void update(Student student) {
        tx.runInTransaction(em -> {
            em.merge(student);
            return null;
        });
    }

    @Override
    public Student findById(Long studentId) {
        return tx.runInTransaction(em ->
                em.find(Student.class, studentId)
        );
    }

    @Override
    public List<Student> findAll() {
        return tx.runInTransaction(em ->
                em.createQuery("FROM Student", Student.class)
                        .getResultList()
        );
    }

    @Override
    public void delete(Long studentId) {
        tx.runInTransaction(em -> {
            Student student = em.find(Student.class, studentId);
            if (student != null) {
                em.remove(student);
            }
            return null;
        });

    }


}
