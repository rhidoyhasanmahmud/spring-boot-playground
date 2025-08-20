# CQRS [in-app projector] Demo with Spring Boot + PostgreSQL

A sample CQRS (Command Query Responsibility Segregation) implementation using Spring Boot 3, Java 21, and two PostgreSQL databases (one for writes, one for reads).

**This project demonstrates:**

- Separate write and read persistence units
- Dual Postgres DB setup (write + read)
- Command side for mutations
- Query side for optimized reads
- Simple projector to sync write events into a read model

## Project Structure

```pgsql
src/main/java/com/codemechanix/cqrs
├─ write/              # Write side (domain + repositories + commands)
│  ├─ domain/          # JPA entities (write_db)
│  ├─ repo/            # Write repositories
│  ├─ handler/         # Command handlers
│  └─ api/             # REST controllers for commands
│
├─ read/               # Read side (denormalized view + queries)
│  ├─ view/            # Read entities (read_db)
│  ├─ repo/            # Read repositories
│  ├─ handler/         # Query handlers
│  └─ api/             # REST controllers for queries
│
├─ projector/          # Sync write → read
│  └─ CustomerProjector.java
│
└─ config/             # Datasource + EMF configs for read/write
```

## Prerequisites

* Java 21
* Maven 3.9+
* Docker + Docker Compose
* psql client (optional for DB inspection)

## 🐳 Running PostgreSQL (write + read)

Docker Compose File 

```yaml
version: '3.8'
services:
  postgres-write:
    image: postgres:16
    container_name: postgres_write
    environment:
      POSTGRES_USER: writer
      POSTGRES_PASSWORD: writer_pass
      POSTGRES_DB: write_db
    ports:
      - "5433:5432"

  postgres-read:
    image: postgres:16
    container_name: postgres_read
    environment:
      POSTGRES_USER: reader
      POSTGRES_PASSWORD: reader_pass
      POSTGRES_DB: read_db
    ports:
      - "5434:5432"
```

Start databases:

```bash
docker compose up -d
```

## Running the Application

```bash
mvn clean spring-boot:run
```

## Command Side (Write DB)

1. Create a Customer 

```bash
curl -X POST http://localhost:8080/commands/customers \
  -H "Content-Type: application/json" \
  -d '{"firstName":"Hasan","lastName":"Mahmud","email":"hasan@example.com"}'
```
✅ Inserts into write_db.customers and projects into read_db.customer_view.

```text
Client->>API: POST /commands/customers
    API->>Handler: CreateCustomerCmd
    Handler->>WTX: begin
    WTX->>WDB: INSERT customers(...)
    WTX-->>Handler: id
    Handler->>Proj: upsert(id, first, last, email)
    Proj->>RTX: begin
    RTX->>RDB: INSERT ... ON CONFLICT UPDATE
    RTX-->>Proj: commit
    WTX-->>Handler: commit
    Handler-->>API: id
    API-->>Client: 200 OK
```

## Query Side (Read DB)

1. Get by ID

```bash
curl http://localhost:8080/queries/customers/1
```

2. Search by name

```bash
curl "http://localhost:8080/queries/customers/search?name=hasan"
```

3. Find by email

```bash
curl "http://localhost:8080/queries/customers/by-email?email=ada@example.com"
```

## How Sync Works

* CustomerCommandHandler saves to write_db.customers.
* CustomerProjector runs immediately after, upserts into read_db.customer_view.
* Queries always hit the read DB for optimized reads.

## Database Schema

Write DB (write_db)

```sql
CREATE TABLE customers (
  id BIGSERIAL PRIMARY KEY,
  first_name TEXT NOT NULL,
  last_name  TEXT NOT NULL,
  email      TEXT NOT NULL UNIQUE,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);
```

Read DB (read_db)

```sql
CREATE TABLE customer_view (
  id BIGINT PRIMARY KEY,
  full_name TEXT NOT NULL,
  email     TEXT NOT NULL
);
```