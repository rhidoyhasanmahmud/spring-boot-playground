package com.codemechanix.cqrs.read.handler;

import com.codemechanix.cqrs.read.repo.CustomerViewRepo;
import com.codemechanix.cqrs.read.view.CustomerView;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class CustomerQueryHandler {
    private final CustomerViewRepo repo;
    public CustomerQueryHandler(CustomerViewRepo repo){ this.repo = repo; }

    @Transactional(readOnly = true, transactionManager = "readTx")
    public CustomerView byId(Long id){ return repo.findById(id).orElse(null); }

    @Transactional(readOnly = true, transactionManager = "readTx")
    public List<CustomerView> search(String name){ return repo.findByFullNameContainingIgnoreCase(name); }

    @Transactional(readOnly = true, transactionManager = "readTx")
    public CustomerView byEmail(String email){ return repo.findByEmail(email); }
}
