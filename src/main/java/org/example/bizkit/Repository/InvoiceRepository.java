package org.example.bizkit.Repository;

import org.example.bizkit.Model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice,Integer> {
    Invoice findInvoiceById(Integer id);
    Invoice findInvoiceByOrderId(Integer orderId);

}
