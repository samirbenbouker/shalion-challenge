package com.shalion.challenge.controller;

import com.shalion.challenge.dto.EnlistmentAcceptedResponse;
import com.shalion.challenge.dto.EnlistmentRequest;
import com.shalion.challenge.dto.EnlistmentStatusResponse;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface EnlistmentController {

    EnlistmentAcceptedResponse startEnlistment(EnlistmentRequest request);
    EnlistmentStatusResponse getStatus(UUID processId);
    Page<EnlistmentStatusResponse> listProcesses(int page, int size, String sort);

}
