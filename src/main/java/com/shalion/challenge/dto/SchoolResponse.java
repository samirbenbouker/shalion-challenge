package com.shalion.challenge.dto;

import java.util.List;

public record SchoolResponse(
        Long id,
        String name,
        Integer maxCapacity,
        List<StudentSummaryResponse> students
) {
    /**
     * Creates a school response without student details.
     *
     * @param id school identifier
     * @param name school name
     * @param maxCapacity configured maximum capacity
     */
    public SchoolResponse(Long id, String name, Integer maxCapacity) {
        this(id, name, maxCapacity, List.of());
    }
}
