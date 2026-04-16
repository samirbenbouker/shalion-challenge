package com.shalion.challenge.service;

import com.shalion.challenge.dto.EnlistmentAcceptedResponse;
import com.shalion.challenge.dto.EnlistmentRequest;
import com.shalion.challenge.dto.EnlistmentStatusResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface EnlistmentService {

    EnlistmentAcceptedResponse startEnlistment(EnlistmentRequest request);
    EnlistmentStatusResponse getStatus(UUID processId);
    Page<EnlistmentStatusResponse> listProcesses(Pageable pageable);

}
