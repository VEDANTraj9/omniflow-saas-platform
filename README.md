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
Tech Stack
Frontend: Next.js 14/15, React, TypeScript, Tailwind CSS, shadcn/ui

Core Business Backend: Java Spring Boot 3, Spring Data JPA, Spring Security, Hibernate

Real-time Gateway: Node.js, Express.js, Socket.IO

AI & Document Service: Python FastAPI

Databases & Cache: PostgreSQL 16 (Relational & Ledgers), Redis 7 (Pub/Sub & Caching)

DevOps & Tools: Docker, Docker Compose, Git, Postman

Core Enterprise Features
Multi-Tenant Isolation: Enforces strict tenant separation across tables using organizational UUID keys.

ACID Financial Transactions: Guarantees consistency across billing calculations and immutable double-entry ledger updates.

Decoupled Event Streaming: Spring Boot publishes transactional events to Redis Pub/Sub, enabling the Node.js layer to broadcast real-time notifications to connected clients.

Smart Document Processing: AI-powered endpoints automatically extract receipt details and line items directly into invoice workflows.

Database Schema (PostgreSQL)
Plaintext
tenants (id, company_name, subdomain, created_at)
   │
   ├──> users (id, tenant_id, email, password_hash, full_name, role)
   │
   └──> invoices (id, tenant_id, invoice_number, customer_name, total_amount, status)
           │
           ├──> invoice_items (id, invoice_id, item_description, quantity, unit_price, subtotal)
           │
           └──> ledger_entries (id, tenant_id, invoice_id, entry_type, amount, created_at)
Local Setup & Installation
1. Database & Cache Initialization
Start PostgreSQL and Redis:

Bash
docker compose up -d
(Or use a local PostgreSQL instance running on port 5432 with database omniflow_db)

2. Run Core Backend (Spring Boot)
Bash
cd backend-core
mvn clean compile
mvn spring-boot:run
Port: 8080 (or 8081)

3. Run Real-Time Service (Node.js)
Bash
cd backend-realtime
npm install
npm run dev
Port: 5000

4. Run AI Microservice (FastAPI)
Bash
cd service-ai
pip install -r requirements.txt
uvicorn main:app --reload --port 8000
Port: 8000

5. Run Frontend (Next.js)
Bash
cd frontend
npm install
npm run dev
Port: 3000

Author
Manikant Kumar

GitHub Profile
'@ | Set-Content -Path "README.md" -Encoding UTF8


---

Upar wala command chalane ke baad direct yeh 3 commands run karein:

```powershell
git add README.md
git commit -m "docs: add complete architecture and setup guide"
git push origin main
Push complete hone ke baad confirm karein, phir agle step Node.js Real-time Socket service par move karte hain!