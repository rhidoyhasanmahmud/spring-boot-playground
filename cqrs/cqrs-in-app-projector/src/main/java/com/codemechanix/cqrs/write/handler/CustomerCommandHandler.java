package com.codemechanix.cqrs.write.handler;

import com.codemechanix.cqrs.read.projector.CustomerProjector;
import com.codemechanix.cqrs.write.command.CreateCustomerCmd;
import com.codemechanix.cqrs.write.domain.Customer;
import com.codemechanix.cqrs.write.repo.CustomerWriteRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerCommandHandler {

    private final CustomerWriteRepo repo;
    private final CustomerProjector projector;

    public CustomerCommandHandler(CustomerWriteRepo repo, CustomerProjector projector) {
        this.repo = repo;
        this.projector = projector;
    }

    @Transactional("writeTx")
    public Long handle(CreateCustomerCmd cmd) {
        var customer = Customer.builder()
                .firstName(cmd.firstName())
                .lastName(cmd.lastName())
                .email(cmd.email())
                .build();

        var saved = repo.save(customer);

        projector.upsert(saved.getId(), cmd.firstName(), cmd.lastName(), cmd.email());

        return saved.getId();
    }
}

