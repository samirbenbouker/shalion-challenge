package com.shalion.challenge.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SchoolRequest(
        @NotBlank(message = "School name is required")
        String name,
        @NotNull(message = "maxCapacity is required")
        @Min(value = 50, message = "maxCapacity must be at least 50")
        @Max(value = 2000, message = "maxCapacity must be at most 2000")
        Integer maxCapacity
) {
}
