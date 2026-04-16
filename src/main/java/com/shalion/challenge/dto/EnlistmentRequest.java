package com.shalion.challenge.dto;

import jakarta.validation.constraints.NotNull;

public record EnlistmentRequest(
        @NotNull(message = "studentId is required")
        Long studentId,
        @NotNull(message = "schoolId is required")
        Long schoolId
) {
}
