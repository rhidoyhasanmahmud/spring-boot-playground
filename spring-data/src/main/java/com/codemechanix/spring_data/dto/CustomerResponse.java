package com.codemechanix.spring_data.dto;

import java.time.Instant;
import java.util.UUID;

public record CustomerResponse(
        Long id,
        UUID publicId,
        String firstName,
        String lastName,
        String email,
        String phone,
        String address,
        Instant createdAt,
        Instant updatedAt
) {}