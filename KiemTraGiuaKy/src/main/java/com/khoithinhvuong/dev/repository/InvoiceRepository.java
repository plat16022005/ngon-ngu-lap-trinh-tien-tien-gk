package com.khoithinhvuong.dev.repository;

import com.khoithinhvuong.dev.model.Invoice;

import java.util.List;

public interface InvoiceRepository {
    void create(Invoice invoice);

    Invoice findById(Long invoiceId);

    List<Invoice> findAll();

    void delete(Long invoiceId);
}
