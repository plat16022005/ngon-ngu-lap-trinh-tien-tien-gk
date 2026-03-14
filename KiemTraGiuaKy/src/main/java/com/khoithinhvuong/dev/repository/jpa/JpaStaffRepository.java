package com.khoithinhvuong.dev.repository.jpa;

import com.khoithinhvuong.dev.config.TransactionManager;
import com.khoithinhvuong.dev.model.Staff;
import com.khoithinhvuong.dev.repository.StaffRepository;

import java.util.List;

public class JpaStaffRepository implements StaffRepository {

    private final TransactionManager tx;

    public JpaStaffRepository(TransactionManager tx) {
        this.tx = tx;
    }

    @Override
    public void create(Staff staff) {

        tx.runInTransaction(em -> {
            em.persist(staff);
            return null;
        });
    }

    @Override
    public void update(Staff staff) {

        tx.runInTransaction(em -> {
            em.merge(staff);
            return null;
        });
    }

    @Override
    public void delete(Long staffId) {

        tx.runInTransaction(em -> {

            Staff staff = em.find(Staff.class, staffId);

            if (staff != null) {
                em.remove(staff);
            }

            return null;
        });
    }

    @Override
    public Staff findById(Long staffId) {

        return tx.runInTransaction(em ->
                em.find(Staff.class, staffId)
        );
    }

    @Override
    public List<Staff> findAll() {

        return tx.runInTransaction(em ->
                em.createQuery("FROM Staff", Staff.class)
                        .getResultList()
        );
    }
    @Override
    public List<Staff> findAllExceptAdmin() {

        return tx.runInTransaction(em ->
                em.createQuery(
                        "SELECT s FROM Staff s WHERE LOWER(s.role) <> 'admin'",
                        Staff.class
                ).getResultList()
        );

    }
}