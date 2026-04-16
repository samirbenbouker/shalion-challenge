package com.shalion.challenge.dto;

import com.shalion.challenge.domain.EnlistmentStatus;

import java.time.Instant;
import java.util.UUID;

public record EnlistmentStatusResponse(
        UUID processId,
        Long studentId,
        Long schoolId,
        EnlistmentStatus status,
        boolean finished,
        Boolean success,
        String message,
        Instant createdAt,
        Instant completedAt
) {
}
