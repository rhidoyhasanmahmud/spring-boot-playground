

# Update Request 

```
curl --location --request PATCH 'http://localhost:8080/api/customers' \
--header 'Content-Type: application/json' \
--header 'Cookie: JSESSIONID=99031C9F5394792A19BEE0613C25BC3E' \
--data-raw '{
    "id": 152,
    "version": 1,
    "firstName": "Hasan",
    "lastName": "Mahmud Rhidoy",
    "email": "hasan@example.com",
    "phone": "0123456789",
    "address": "Kuala Lumpur"
}'
```

# Create Request 

```
curl --location 'http://localhost:8080/api/customers' \
--header 'Content-Type: application/json' \
--header 'Cookie: JSESSIONID=99031C9F5394792A19BEE0613C25BC3E' \
--data-raw '{
    "firstName": "Hasan",
    "lastName": "Mahmud",
    "email": "hasan@example.com",
    "phone": "0123456789",
    "address": "Kuala Lumpur"
}'
```

:: 1) find by email
curl "http://localhost:8080/api/customers/by-email?email=hasan@example.com"

:: 2) find by ids
curl -X POST http://localhost:8080/api/customers/by-ids ^
-H "Content-Type: application/json" ^
-d "[1,2,3]"

:: 3) last name contains
curl "http://localhost:8080/api/customers/search/last?q=mah"

:: 4) first OR last name contains
curl "http://localhost:8080/api/customers/search/name?q=has"

:: 5) created between (UTC instants)
curl "http://localhost:8080/api/customers/created-between?from=2025-08-18T00:00:00Z&to=2025-08-20T00:00:00Z"

:: 6) top 5 recent
curl "http://localhost:8080/api/customers/top5"

:: 7) count active
curl "http://localhost:8080/api/customers/count"

:: 8) list active (paging + sort)
curl "http://localhost:8080/api/customers?page=0&size=20&sort=createdAt,desc"

:: 9) stream NDJSON
curl "http://localhost:8080/api/customers/stream"
