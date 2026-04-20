package com.shalion.challenge.service;

import com.shalion.challenge.dto.EnlistmentAcceptedResponse;
import com.shalion.challenge.dto.EnlistmentRequest;
import com.shalion.challenge.dto.EnlistmentStatusResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface EnlistmentService {

    /**
     * Starts an asynchronous student enlistment process.
     *
     * @param request enlistment payload
     * @return accepted process response
     */
    EnlistmentAcceptedResponse startEnlistment(EnlistmentRequest request);

    /**
     * Returns the status for a process identifier.
     *
     * @param processId process identifier
     * @return enlistment status response
     */
    EnlistmentStatusResponse getStatus(UUID processId);

    /**
     * Returns paged enlistment processes.
     *
     * @param pageable pagination configuration
     * @return paged process statuses
     */
    Page<EnlistmentStatusResponse> listProcesses(Pageable pageable);

}
