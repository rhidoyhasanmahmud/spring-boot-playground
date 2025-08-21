package com.codemechanix.cqrs.command.service;

import com.codemechanix.cqrs.command.model.Customer;
import com.codemechanix.cqrs.command.repo.CustomerCommandRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerCommandService {
    private final CustomerCommandRepo repo;

    public Customer createCustomer(Customer c) {
        return repo.save(c); // Write DB
    }
}