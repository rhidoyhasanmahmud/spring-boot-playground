package com.codemechanix.spring_data.service;

import com.codemechanix.spring_data.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface CustomerServiceV1 {
    Customer create(Customer req);
    Customer patch(Customer req);

    Optional<Customer> findByEmail(String email);
    List<Customer> findByIds(Collection<Long> ids);
    List<Customer> searchLastNameContains(String part);
    List<Customer> searchNameContains(String q);
    List<Customer> findCreatedBetween(Instant from, Instant to);
    List<Customer> top5Recent();
    long countActive();
    Page<Customer> listActive(Pageable pageable);
    java.util.stream.Stream<Customer> streamAllActive();
}
