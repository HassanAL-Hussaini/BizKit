package org.example.bizkit.Service;

import lombok.RequiredArgsConstructor;
import org.example.bizkit.Api.ApiException;
import org.example.bizkit.Model.Admin;
import org.example.bizkit.Model.Invoice;
import org.example.bizkit.Repository.InvoiceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final AdminService adminService;

    // ===================== READ =====================
    // Admin only - get all invoices
    public List<?> getAllInvoices(Integer adminId) {
        adminService.getAdminByIdAndCheckIfExist(adminId);
        return invoiceRepository.findAll();
    }
    //Done get single invoice by invoice id
    public Invoice getInvoice(Integer invoiceId) {
        Invoice invoice = getInvoiceByIdAndCheckIfExist(invoiceId);
        return invoice;
    }

    // ===================== CREATE =====================
    //No create

    // ===================== UPDATE =====================
    // Admin only - update invoice by id
    public void updateInvoice(Integer adminId, Integer invoiceIdUpdated, Invoice newInvoice) {
        adminService.getAdminByIdAndCheckIfExist(adminId);
        Invoice oldInvoice = getInvoiceByIdAndCheckIfExist(invoiceIdUpdated);
        // adjust fields as per your Invoice model
        oldInvoice.setOrderId(newInvoice.getOrderId());
        oldInvoice.setAmount(newInvoice.getAmount());
        oldInvoice.setIssuedDate(newInvoice.getIssuedDate());
        oldInvoice.setDueDate(newInvoice.getDueDate());
        invoiceRepository.save(oldInvoice);
    }

    // ===================== DELETE =====================
    // Admin only - delete invoice by id
    public void deleteInvoice(Integer adminId, Integer invoiceIdDeleted) {
        adminService.getAdminByIdAndCheckIfExist(adminId);
        Invoice invoice = invoiceRepository.findInvoiceById(invoiceIdDeleted);
        if (invoice == null) throw new ApiException("Invoice Not Found");
        invoiceRepository.delete(invoice);
    }

    // ===================== INTERNAL HELPERS =====================
    protected Invoice getInvoiceByIdAndCheckIfExist(Integer id) {
        Invoice invoice = invoiceRepository.findInvoiceById(id);
        if (invoice == null) throw new ApiException("Invoice Not Found");
        return invoice;
    }
}
