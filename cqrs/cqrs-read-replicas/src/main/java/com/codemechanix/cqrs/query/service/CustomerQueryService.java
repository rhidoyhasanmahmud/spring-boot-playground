package com.codemechanix.cqrs.query.service;

import com.codemechanix.cqrs.command.model.Customer;
import com.codemechanix.cqrs.query.repo.CustomerQueryRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerQueryService {
    private final CustomerQueryRepo repo;

    public List<Customer> search(String lastName) {
        return repo.findByLastNameContainingIgnoreCase(lastName); // Read DB
    }
}