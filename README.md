@'
# OmniFlow — Enterprise Multi-Tenant SaaS & Real-Time Billing Engine

OmniFlow is a distributed, event-driven enterprise billing and inventory management platform. Built to demonstrate production-grade system design, it features strict row-level multi-tenancy, ACID-compliant double-entry ledger bookkeeping, real-time telemetry streaming, and automated document extraction.

---

## High-Level System Architecture

```text
               +-----------------------------+
               |  Next.js 14 Web Dashboard   | (App Router, Tailwind, TypeScript)
               +--------------+--------------+
                              |
        +---------------------+---------------------+
        |                                           |
        v (REST API)                                v (OCR / Extraction)
+-----------------------+                   +-----------------------+
|  Spring Boot 3 Core   |                   |  FastAPI AI Service   |
|  (Billing & Ledgers)  |                   |  (Document Parsing)   |
+-----------+-----------+                   +-----------------------+
            |
    (Redis Pub/Sub)
            |
            v
+-----------------------+
|  Node.js Real-time    |
|  (Socket.IO Service)  |
+-----------+-----------+
            |
            +----------------- WebSocket Broadcast -----------------> (Client Dashboard)


tenants (id, company_name, subdomain, created_at)
   │
   ├──> users (id, tenant_id, email, password_hash, full_name, role)
   │
   └──> invoices (id, tenant_id, invoice_number, customer_name, total_amount, status)
           │
           ├──> invoice_items (id, invoice_id, item_description, quantity, unit_price, subtotal)
           │
           └──> ledger_entries (id, tenant_id, invoice_id, entry_type, amount, created_at)

docker compose up -d