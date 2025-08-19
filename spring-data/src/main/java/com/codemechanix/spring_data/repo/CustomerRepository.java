package com.codemechanix.spring_data.repo;

import com.codemechanix.spring_data.model.Customer;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {

    Optional<Customer> findByEmailAndDeletedFalse(String email);

    List<Customer> findByIdInAndDeletedFalse(Collection<Long> ids);

    List<Customer> findByLastNameContainingIgnoreCaseAndDeletedFalse(String part);

    List<Customer> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String q1, String q2);

    List<Customer> findByCreatedAtBetweenAndDeletedFalse(Instant from, Instant to);

    List<Customer> findTop5ByDeletedFalseOrderByCreatedAtDesc();

    long countByDeletedFalse();

    Page<Customer> findAllByDeletedFalse(Pageable pageable);

    @Query("select c from Customer c where c.deleted = false")
    java.util.stream.Stream<Customer> streamAllActive();
}
