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

## How We Sync Write ➜ Read

**1. In-App Projector (what we use now)**

- How: After a write succeeds, app upserts the read model in another transaction. 
- Use when: Prototyping; single service; you want immediate demo value. 
- Trade-offs: Two writes in one request → add retries; not ideal if read DB is flaky.

**2. Transactional Outbox + Poller**

- How: Write data and an outbox event in the same transaction; a background job polls outbox and updates read DB. 
- Use when: First production-ready step; durable; easy to reason about. 
- Trade-offs: Extra table + background worker.

**3. Outbox + Message Broker (Kafka/RabbitMQ/SQS)**

- How: Same outbox, but a relay publishes to a broker; consumers project to read DB.
- Use when: Multiple services; high throughput; need fan-out.
- Trade-offs: More infra/ops; message ordering/at-least-once semantics to handle.

4. Change Data Capture (CDC) with Debezium / Kafka Connect

- How: Stream DB changes from WAL; consumers project.
- Use when: Don’t want app to emit events; near real-time.
- Trade-offs: Operate CDC stack; schema evolution discipline.

5. Postgres Logical Replication (publication/subscription)

- How: Postgres replicates tables; on the read side use triggers/materialized views to shape data.
- Use when: Pure Postgres setup; low latency; minimal app code.
- Trade-offs: Cross-DB networking/permissions; transforms live in DB.

6. DB Triggers (inside write DB)

- How: Triggers on source tables keep projection tables in sync (same DB or via FDW/dblink).
- Use when: Everything in one DB; want instant sync.
- Trade-offs: Logic hidden in DB; harder to test/version alongside app.

7. Materialized Views (refresh)

- How: Read model is a materialized view; refresh on a schedule or on demand.
- Use when: Reporting/analytics; some staleness is fine.
- Trade-offs: Consistency = refresh cadence.

8. Read Replicas (hot standby)

- How: Point reads to a replica of the write DB.
- Use when: Need read scaling, not a different shape.
- Trade-offs: Same schema; replication lag possible.

9. Batch ETL (hourly/nightly jobs)

- How: Periodic job copies/transforms data into read store.
- Use when: Back-office, dashboards, cheap/simple.
- Trade-offs: Minutes–hours of lag.

10. Event Sourcing + Projections

- How: Store domain events; build read models by replaying.
- Use when: Need full history, time travel, rebuilds.
- Trade-offs: Biggest mindset/architecture shift.

### Comparison 

| Approach            | Consistency                       |  Latency | Complexity | Notes                              |
| ------------------- | --------------------------------- | -------: | ---------: | ---------------------------------- |
| In-app projector    | Eventual (very short)             |      Low |        Low | Dual write in request; add retries |
| Outbox + Poller     | Eventual (safe)                   |  Low–Med |    Low–Med | **Most common first prod step**    |
| Outbox + Broker     | Eventual (safe)                   |      Low |   Med–High | Scale, fan-out, multiple consumers |
| CDC/Debezium        | Eventual                          |      Low |       High | No app changes on write path       |
| Logical replication | Eventual                          | Very low |        Med | DB-driven, transforms in DB        |
| Read replica        | Eventual                          |      Low |        Low | Same schema only                   |
| Mat. views / Batch  | Eventual                          | Med–High |        Low | Great for analytics/reporting      |
| Event sourcing      | Eventually consistent projections |  Low–Med |       High | Full history/rebuilds              |
