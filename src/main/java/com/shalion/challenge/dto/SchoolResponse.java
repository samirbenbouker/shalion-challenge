package com.shalion.challenge.dto;

import java.util.List;

public record SchoolResponse(
        Long id,
        String name,
        Integer maxCapacity,
        List<StudentSummaryResponse> students
) {
    public SchoolResponse(Long id, String name, Integer maxCapacity) {
        this(id, name, maxCapacity, List.of());
    }
}
