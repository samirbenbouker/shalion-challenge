package com.shalion.challenge.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record StudentRequest(
        @NotBlank(message = "Student name is required")
        String name,
        @NotNull(message = "schoolId is required")
        Long schoolId
) {
}
