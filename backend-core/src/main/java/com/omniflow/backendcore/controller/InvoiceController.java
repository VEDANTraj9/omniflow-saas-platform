package com.omniflow.backendcore.controller;

import com.omniflow.backendcore.dto.CreateInvoiceRequest;
import com.omniflow.backendcore.model.Invoice;
import com.omniflow.backendcore.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/invoices")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class InvoiceController {

    private final InvoiceService invoiceService;

    @PostMapping
    public ResponseEntity<Invoice> createInvoice(@RequestBody CreateInvoiceRequest request) {
        Invoice invoice = invoiceService.createInvoice(request);
        return ResponseEntity.ok(invoice);
    }

    @GetMapping("/tenant/{tenantId}")
    public ResponseEntity<List<Invoice>> getTenantInvoices(@PathVariable UUID tenantId) {
        List<Invoice> invoices = invoiceService.getInvoicesByTenant(tenantId);
        return ResponseEntity.ok(invoices);
    }
}