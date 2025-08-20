package com.codemechanix.cqrs.read.repo;

import com.codemechanix.cqrs.read.view.CustomerView;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CustomerViewRepo extends JpaRepository<CustomerView, Long> {
    List<CustomerView> findByFullNameContainingIgnoreCase(String q);
    CustomerView findByEmail(String email);
}
