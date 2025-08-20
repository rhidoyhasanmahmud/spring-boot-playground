package com.codemechanix.cqrs.read.projector;

import com.codemechanix.cqrs.read.view.CustomerView;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerProjector {

    @PersistenceContext(unitName = "readPU")
    private EntityManager readEm;

    @Transactional(transactionManager = "readTx")
    public void upsert(Long id, String first, String last, String email) {
        var view = CustomerView.builder()
                .id(id)
                .fullName(first + " " + last)
                .email(email)
                .build();

        readEm.merge(view);
    }
}
