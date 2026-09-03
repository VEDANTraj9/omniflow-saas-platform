package com.omniflow.backendcore.service;

import com.omniflow.backendcore.dto.CreateInvoiceRequest;
import com.omniflow.backendcore.model.Invoice;
import com.omniflow.backendcore.repository.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final StringRedisTemplate redisTemplate;

    @Transactional
    public Invoice createInvoice(CreateInvoiceRequest request) {
        // 1. Database me invoice save karna
        Invoice invoice = Invoice.builder()
                .tenantId(request.getTenantId())
                .invoiceNumber(request.getInvoiceNumber())
                .customerName(request.getCustomerName())
                .totalAmount(request.getTotalAmount())
                .status("PENDING")
                .createdAt(LocalDateTime.now())
                .build();

        Invoice savedInvoice = invoiceRepository.save(invoice);

        // 2. Real-time broadcast ke liye Redis Pub/Sub par event push karna
        String message = String.format("New invoice %s generated for %s (Amount: ₹%s)",
                savedInvoice.getInvoiceNumber(),
                savedInvoice.getCustomerName(),
                savedInvoice.getTotalAmount());

        try {
            redisTemplate.convertAndSend("invoice-events", message);
        } catch (Exception e) {
            System.err.println("Redis broadcast skipped: " + e.getMessage());
        }

        return savedInvoice;
    }

    public List<Invoice> getInvoicesByTenant(UUID tenantId) {
        return invoiceRepository.findByTenantId(tenantId);
    }
}