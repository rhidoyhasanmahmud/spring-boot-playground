package com.codemechanix.spring_data.dto;

import java.util.Objects;
import java.util.stream.Stream;

public record CustomerPatchRequest(
        String firstName,
        String lastName,
        String email,
        String phone,
        String address
) {
    public boolean isEmpty() {
        return Stream.of(firstName, lastName, email, phone, address).allMatch(Objects::isNull);
    }
}
