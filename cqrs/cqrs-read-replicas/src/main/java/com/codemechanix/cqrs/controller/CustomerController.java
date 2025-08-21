package com.codemechanix.cqrs.controller;

import com.codemechanix.cqrs.command.model.Customer;
import com.codemechanix.cqrs.command.service.CustomerCommandService;
import com.codemechanix.cqrs.query.service.CustomerQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerCommandService commandService;
    private final CustomerQueryService queryService;

    public CustomerController(CustomerCommandService cs, CustomerQueryService qs) {
        this.commandService = cs;
        this.queryService = qs;
    }

    @PostMapping
    public ResponseEntity<Customer> create(@RequestBody Customer c) {
        return ResponseEntity.ok(commandService.createCustomer(c));
    }

    @GetMapping
    public ResponseEntity<List<Customer>> search(@RequestParam String lastName) {
        return ResponseEntity.ok(queryService.search(lastName));
    }
}
