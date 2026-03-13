package com.khoithinhvuong.dev.service;

import com.khoithinhvuong.dev.config.TransactionManager;
import com.khoithinhvuong.dev.model.Invoice;
import com.khoithinhvuong.dev.repository.InvoiceRepository;
import com.khoithinhvuong.dev.repository.jpa.JpaInvoiceRepository;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

public class InvoiceService {

    private static final TransactionManager tx = new TransactionManager();
    private final InvoiceRepository invoiceRepository =
            new JpaInvoiceRepository(tx);


    public void createInvoice(Invoice invoice) {

        boolean exist = invoiceRepository.findAll()
                .stream()
                .anyMatch(i -> i.getPayment().getPaymentId()
                        .equals(invoice.getPayment().getPaymentId()));

        if (exist) {
            throw new RuntimeException("Payment này đã có invoice!");
        }

        invoice.setIssueDate(LocalDate.now());
        invoiceRepository.create(invoice);
    }


    public List<Invoice> getInvoicesByStudent(Long studentId) {

        return invoiceRepository.findAll()
                .stream()
                .filter(i -> i.getStudent().getStudentId().equals(studentId))
                .toList();
    }


    public List<Invoice> getAllInvoicesSorted() {

        return invoiceRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Invoice::getIssueDate))
                .toList();
    }


    public Invoice getInvoiceByPayment(Long paymentId) {

        return invoiceRepository.findAll()
                .stream()
                .filter(i -> i.getPayment().getPaymentId().equals(paymentId))
                .findFirst()
                .orElse(null);
    }


    public void deleteInvoicesByStudent(Long studentId) {

        invoiceRepository.findAll()
                .stream()
                .filter(i -> i.getStudent().getStudentId().equals(studentId))
                .forEach(i -> invoiceRepository.delete(i.getInvoiceId()));
    }
}