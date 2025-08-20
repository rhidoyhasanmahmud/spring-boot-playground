package com.codemechanix.spring_data.controller;

import com.codemechanix.spring_data.dto.CreateCustomer;
import com.codemechanix.spring_data.dto.CustomerResponse;
import com.codemechanix.spring_data.dto.UpdateCustomer;
import com.codemechanix.spring_data.service.CustomerServiceV2;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/customers")
public class CustomerControllerV2 {

    @Autowired
    private CustomerServiceV2 customerService;

    @PostMapping
    public ResponseEntity<CustomerResponse> createCustomer(@Valid @RequestBody CreateCustomer createCustomer) {
        CustomerResponse created = customerService.createCustomer(createCustomer);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> updateCustomer(@PathVariable Long id,
                                                           @Valid @RequestBody UpdateCustomer updateCustomer) {
        CustomerResponse updated = customerService.updateCustomer(id, updateCustomer);
        return ResponseEntity.ok(updated);
    }
}