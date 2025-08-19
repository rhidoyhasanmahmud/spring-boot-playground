package com.codemechanix.spring_data.service;

import com.codemechanix.spring_data.model.Customer;
import com.codemechanix.spring_data.repo.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repo;

    @Override
    public Customer create(Customer req) {
        return repo.save(req);
    }

    @Override
    public Customer patch(Customer req) {
        return repo.save(req);
    }

    @Override public Optional<Customer> findByEmail(String email) {
        return repo.findByEmailAndDeletedFalse(email);
    }

    @Override public List<Customer> findByIds(Collection<Long> ids) {
        return repo.findByIdInAndDeletedFalse(ids);
    }

    @Override public List<Customer> searchLastNameContains(String part) {
        return repo.findByLastNameContainingIgnoreCaseAndDeletedFalse(part);
    }

    @Override public List<Customer> searchNameContains(String q) {
        return repo.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(q, q);
    }

    @Override public List<Customer> findCreatedBetween(Instant from, Instant to) {
        return repo.findByCreatedAtBetweenAndDeletedFalse(from, to);
    }

    @Override public List<Customer> top5Recent() {
        return repo.findTop5ByDeletedFalseOrderByCreatedAtDesc();
    }

    @Override public long countActive() {
        return repo.countByDeletedFalse();
    }

    @Override public Page<Customer> listActive(Pageable pageable) {
        return repo.findAllByDeletedFalse(pageable);
    }

    @Override public java.util.stream.Stream<Customer> streamAllActive() {
        return repo.streamAllActive();
    }
}
