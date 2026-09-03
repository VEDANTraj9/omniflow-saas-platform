package com.omniflow.backendcore.repository;

import com.omniflow.backendcore.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {
    // Row-level isolation: Tenant ID ke bina records fetch nahi honge
    List<Invoice> findByTenantId(UUID tenantId);
}