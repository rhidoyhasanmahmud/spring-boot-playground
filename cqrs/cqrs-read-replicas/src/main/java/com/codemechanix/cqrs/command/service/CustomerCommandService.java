package com.codemechanix.cqrs.command.service;

import com.codemechanix.cqrs.command.model.Customer;
import com.codemechanix.cqrs.command.repo.CustomerCommandRepo;
import org.springframework.stereotype.Service;

@Service
public class CustomerCommandService {
    private final CustomerCommandRepo repo;

    public CustomerCommandService(CustomerCommandRepo repo) {
        this.repo = repo;
    }

    public Customer createCustomer(Customer c) {
        return repo.save(c); // Write DB
    }
}
