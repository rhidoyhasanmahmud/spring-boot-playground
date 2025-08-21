package com.codemechanix.cqrs.command.repo;

import com.codemechanix.cqrs.command.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerCommandRepo extends JpaRepository<Customer, Long> {
}
