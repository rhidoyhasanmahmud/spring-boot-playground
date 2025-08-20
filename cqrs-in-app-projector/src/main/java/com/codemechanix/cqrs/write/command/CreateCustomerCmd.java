package com.codemechanix.cqrs.write.command;

import jakarta.validation.constraints.*;

public record CreateCustomerCmd(
        @NotBlank String firstName,
        @NotBlank String lastName,
        @Email @NotBlank String email
) {}
