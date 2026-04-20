package com.shalion.challenge.controller;

import com.shalion.challenge.dto.EnlistmentAcceptedResponse;
import com.shalion.challenge.dto.EnlistmentRequest;
import com.shalion.challenge.dto.EnlistmentStatusResponse;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface EnlistmentController {

    /**
     * Starts an asynchronous enlistment process.
     *
     * @param request enlistment payload
     * @return accepted process metadata
     */
    EnlistmentAcceptedResponse startEnlistment(EnlistmentRequest request);

    /**
     * Returns the current status of an enlistment process.
     *
     * @param processId process identifier
     * @return process status
     */
    EnlistmentStatusResponse getStatus(UUID processId);

    /**
     * Returns a paged list of enlistment processes.
     *
     * @param page zero-based page index
     * @param size page size
     * @param sort sort expression
     * @return paged enlistment statuses
     */
    Page<EnlistmentStatusResponse> listProcesses(int page, int size, String sort);

}
