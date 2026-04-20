package com.shalion.challenge.controller.impl;

import com.shalion.challenge.controller.EnlistmentController;
import com.shalion.challenge.dto.EnlistmentAcceptedResponse;
import com.shalion.challenge.dto.EnlistmentRequest;
import com.shalion.challenge.dto.EnlistmentStatusResponse;
import com.shalion.challenge.service.EnlistmentService;
import com.shalion.challenge.util.PaginationUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/enlistments")
public class EnlistmentControllerImpl implements EnlistmentController {

    @Autowired
    private EnlistmentService enlistmentService;

    /**
     * Starts an asynchronous enlistment process.
     *
     * @param request enlistment payload
     * @return accepted process information
     */
    @Override
    @Operation(summary = "Start asynchronous enlistment of a student into a school")
    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public EnlistmentAcceptedResponse startEnlistment(@Valid @RequestBody EnlistmentRequest request) {
        return enlistmentService.startEnlistment(request);
    }

    /**
     * Returns the current state of a specific enlistment process.
     *
     * @param processId enlistment process id
     * @return process state
     */
    @Override
    @Operation(summary = "Get enlistment process status and result")
    @GetMapping("/{processId}")
    public EnlistmentStatusResponse getStatus(@PathVariable UUID processId) {
        return enlistmentService.getStatus(processId);
    }

    /**
     * Returns paged enlistment process records.
     *
     * @param page zero-based page index
     * @param size page size
     * @param sort sort expression
     * @return paged enlistment statuses
     */
    @Override
    @Operation(summary = "List all enlistment processes with pagination")
    @GetMapping
    public Page<EnlistmentStatusResponse> listProcesses(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "20") int size,
            @Parameter(description = "Sort format: field,direction", example = "createdAt,desc")
            @RequestParam(required = false, defaultValue = "createdAt,desc") String sort
    ) {
        Pageable pageable = PaginationUtils.toPageable(page, size, sort, "createdAt");
        return enlistmentService.listProcesses(pageable);
    }
}
