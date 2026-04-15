package com.shalion.challenge.dto;

public record StudentResponse(
        Long id,
        String name,
        Long schoolId,
        String schoolName
) {
}
