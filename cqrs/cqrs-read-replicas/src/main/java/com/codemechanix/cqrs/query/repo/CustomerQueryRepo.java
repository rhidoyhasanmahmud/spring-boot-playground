package com.codemechanix.cqrs.query.repo;

import com.codemechanix.cqrs.command.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerQueryRepo extends JpaRepository<Customer, Long> {
    List<Customer> findByLastNameContainingIgnoreCase(String lastName);
}
