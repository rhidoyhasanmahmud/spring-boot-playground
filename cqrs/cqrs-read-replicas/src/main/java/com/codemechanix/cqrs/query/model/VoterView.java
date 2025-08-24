package com.codemechanix.cqrs.query.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Immutable;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "voters")
@Immutable
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class VoterView {
    @Id
    private Integer id;

    private String username;

    @Column(name = "user_nid")
    private String userNid;

    @Column(columnDefinition = "text")
    private String address;

    private String phone;

    private LocalDate dob;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;
}
