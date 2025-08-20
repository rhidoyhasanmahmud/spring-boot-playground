package com.codemechanix.cqrs.read.view;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "customer_view")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CustomerView {
    @Id
    private Long id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String email;
}

