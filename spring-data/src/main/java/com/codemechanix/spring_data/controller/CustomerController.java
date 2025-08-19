package com.codemechanix.spring_data.controller;

import com.codemechanix.spring_data.model.Customer;
import com.codemechanix.spring_data.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.OutputStream;
import java.time.Instant;
import java.util.*;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Customer create(@RequestBody Customer req) {
        return service.create(req);
    }

    @PatchMapping
    public Customer patch(@RequestBody Customer req) {
        return service.patch(req);
    }

    private final ObjectMapper objectMapper; // for stream ndjson

    // 1) find by email
    @GetMapping("/by-email")
    public Optional<Customer> byEmail(@RequestParam String email) {
        return service.findByEmail(email);
    }

    // 2) find by ids (IN)
    @PostMapping("/by-ids")
    public List<Customer> byIds(@RequestBody Collection<Long> ids) {
        return service.findByIds(ids);
    }

    // 3) last name contains
    @GetMapping("/search/last")
    public List<Customer> lastNameContains(@RequestParam String q) {
        return service.searchLastNameContains(q);
    }

    // 4) first OR last name contains
    @GetMapping("/search/name")
    public List<Customer> nameContains(@RequestParam String q) {
        return service.searchNameContains(q);
    }

    // 5) created between (ISO-8601 instants)
    @GetMapping("/created-between")
    public List<Customer> createdBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to) {
        return service.findCreatedBetween(from, to);
    }

    // 6) top 5 recent
    @GetMapping("/top5")
    public List<Customer> top5() {
        return service.top5Recent();
    }

    // 7) count active
    @GetMapping("/count")
    public Map<String, Long> count() {
        return Map.of("activeCount", service.countActive());
    }

    // 8) list active (paging via ?page=&size=&sort=)
    @GetMapping
    public Page<Customer> list(Pageable pageable) {
        return service.listActive(pageable);
    }

    // 9) stream all active as NDJSON
    @GetMapping(value = "/stream", produces = "application/x-ndjson")
    public StreamingResponseBody streamAllActive() {
        return (OutputStream out) -> {
            try (var stream = service.streamAllActive()) {
                stream.forEach(c -> {
                    try {
                        var json = objectMapper.writeValueAsBytes(c);
                        out.write(json);
                        out.write('\n');
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        };
    }
}
