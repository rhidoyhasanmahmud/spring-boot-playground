package com.codemechanix.cqrs.command.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "voters")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Voter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 50, nullable = false)
    private String username;

    @Column(name = "user_nid", length = 20, nullable = false, unique = true)
    private String userNid;

    @Column(columnDefinition = "text")
    private String address;

    @Column(length = 20)
    private String phone;

    private LocalDate dob;

    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @PrePersist
    void prePersist() {
        var now = OffsetDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    void preUpdate() { updatedAt = OffsetDateTime.now(); }
}
