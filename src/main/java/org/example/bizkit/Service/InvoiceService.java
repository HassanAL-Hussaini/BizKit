package org.example.bizkit.Service;

import lombok.RequiredArgsConstructor;
import org.example.bizkit.Api.ApiException;
import org.example.bizkit.Model.Invoice;
import org.example.bizkit.Repository.InvoiceRepository;
import org.example.bizkit.Repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final AdminService adminService;
    // شلنا OrderService واستبدلناه بـ OrderRepository لو احتجت تحقق
    private final OrderRepository orderRepository;

    // ===================== READ =====================
    public List<?> getAllInvoices(Integer adminId) {
        adminService.getAdminByIdAndCheckIfExist(adminId);
        return invoiceRepository.findAll();
    }

    public Invoice getInvoice(Integer invoiceId) {
        return getInvoiceByIdAndCheckIfExist(invoiceId);
    }

    // ===================== CREATE =====================
    public void addInvoice(Integer orderId, Double total) {
        // (اختياري) تأكد إن الطلب موجود بدون استدعاء OrderService
        if (!orderRepository.existsById(orderId)) {
            throw new ApiException("Order not found");
        }

        Invoice invoice = new Invoice();
        invoice.setOrderId(orderId);
        invoice.setAmount(total);
        invoice.setIssuedDate(LocalDate.now());
        invoice.setDueDate(LocalDate.now().plusDays(7));
        invoiceRepository.save(invoice);
    }

    // ===================== UPDATE =====================
    public void updateInvoice(Integer adminId, Integer invoiceIdUpdated, Invoice newInvoice) {
        adminService.getAdminByIdAndCheckIfExist(adminId);
        Invoice oldInvoice = getInvoiceByIdAndCheckIfExist(invoiceIdUpdated);

        // (اختياري) تحقّق من الطلب الجديد
        if (!orderRepository.existsById(newInvoice.getOrderId())) {
            throw new ApiException("Order not found for invoice update");
        }

        oldInvoice.setOrderId(newInvoice.getOrderId());
        oldInvoice.setAmount(newInvoice.getAmount());
        oldInvoice.setIssuedDate(newInvoice.getIssuedDate());
        oldInvoice.setDueDate(newInvoice.getDueDate());
        invoiceRepository.save(oldInvoice);
    }

    // ===================== INTERNAL HELPERS =====================
    protected Invoice getInvoiceByIdAndCheckIfExist(Integer id) {
        Invoice invoice = invoiceRepository.findInvoiceById(id);
        if (invoice == null) throw new ApiException("Invoice Not Found");
        return invoice;
    }
}
