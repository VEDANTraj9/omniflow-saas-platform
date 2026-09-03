package com.omniflow.backendcore.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class CreateInvoiceRequest {
    private UUID tenantId;
    private String invoiceNumber;
    private String customerName;
    private BigDecimal totalAmount;
}