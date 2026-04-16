package com.shalion.challenge.dto;

import com.shalion.challenge.domain.EnlistmentStatus;

import java.util.UUID;

public record EnlistmentAcceptedResponse(
        UUID processId,
        EnlistmentStatus status
) {
}
