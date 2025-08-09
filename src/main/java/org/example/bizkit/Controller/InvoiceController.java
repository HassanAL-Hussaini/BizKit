package org.example.bizkit.Controller;

import lombok.RequiredArgsConstructor;
import org.example.bizkit.Api.ApiResponse;
import org.example.bizkit.Service.InvoiceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;

    // ===================== READ =====================
    @GetMapping("/get-all/{adminId}")
    public ResponseEntity<?> getAllInvoices(@PathVariable Integer adminId) {
        return ResponseEntity.ok(invoiceService.getAllInvoices(adminId));
    }

    // ===================== CREATE =====================
    //No Creation

    // ===================== UPDATE =====================
    //No Update


    // ===================== DELETE =====================
    @DeleteMapping("/delete/{adminId}/{invoiceId}")
    public ResponseEntity<?> deleteInvoice(@PathVariable Integer adminId,
                                           @PathVariable Integer invoiceId) {
        invoiceService.deleteInvoice(adminId, invoiceId);
        return ResponseEntity.ok(new ApiResponse("Invoice deleted successfully"));
    }
}
