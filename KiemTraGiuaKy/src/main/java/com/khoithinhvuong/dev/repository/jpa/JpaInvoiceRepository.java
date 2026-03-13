package com.khoithinhvuong.dev.repository.jpa;

import com.khoithinhvuong.dev.config.TransactionManager;
import com.khoithinhvuong.dev.model.Invoice;
import com.khoithinhvuong.dev.repository.InvoiceRepository;

import java.util.List;

public class JpaInvoiceRepository implements InvoiceRepository {
    private final TransactionManager tx;

    public JpaInvoiceRepository(TransactionManager tx) {
        this.tx = tx;
    }

    @Override
    public void create(Invoice invoice) {
        tx.runInTransaction(em -> {
            em.persist(invoice);
            return null;
        });
    }

    @Override
    public Invoice findById(Long invoiceId) {
        return tx.runInTransaction(em ->
                em.find(Invoice.class, invoiceId)
        );
    }

    @Override
    public List<Invoice> findAll() {
        return tx.runInTransaction(em ->
                em.createQuery("FROM Invoice", Invoice.class)
                        .getResultList()
        );
    }

    @Override
    public void delete(Long invoiceId) {
        tx.runInTransaction(em -> {
            Invoice invoice = em.find(Invoice.class, invoiceId);
            if (invoice != null) {
                em.remove(invoice);
            }
            return null;
        });
    }
}
