package com.codemechanix.cqrs.write.repo;

import com.codemechanix.cqrs.write.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerWriteRepo extends JpaRepository<Customer, Long> { }
