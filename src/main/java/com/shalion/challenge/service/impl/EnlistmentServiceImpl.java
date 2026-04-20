package com.shalion.challenge.service.impl;

import com.shalion.challenge.dto.EnlistmentAcceptedResponse;
import com.shalion.challenge.dto.EnlistmentRequest;
import com.shalion.challenge.dto.EnlistmentStatusResponse;
import com.shalion.challenge.domain.EnlistmentProcess;
import com.shalion.challenge.domain.EnlistmentStatus;
import com.shalion.challenge.exception.NotFoundException;
import com.shalion.challenge.mapper.EnlistmentMapper;
import com.shalion.challenge.repository.EnlistmentProcessRepository;
import com.shalion.challenge.service.EnlistmentService;
import com.shalion.challenge.util.AppMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.UUID;

@Service
public class EnlistmentServiceImpl implements EnlistmentService {

    @Autowired
    private EnlistmentProcessRepository enlistmentProcessRepository;

    @Autowired
    private EnlistmentAsyncWorker enlistmentAsyncWorker;

    @Autowired
    private EnlistmentMapper enlistmentMapper;

    /**
     * Persists a new enlistment process in pending state and schedules async execution.
     *
     * @param request enlistment request payload
     * @return accepted process response
     */
    @Override
    @Transactional
    public EnlistmentAcceptedResponse startEnlistment(EnlistmentRequest request) {
        EnlistmentProcess process = new EnlistmentProcess();
        process.setStudentId(request.studentId());
        process.setSchoolId(request.schoolId());
        process.setStatus(EnlistmentStatus.PENDING);
        process.setFinished(false);
        process.setSuccess(null);
        process.setMessage(AppMessages.ENLISTMENT_REQUEST_ACCEPTED_MESSAGE);

        EnlistmentProcess saved = enlistmentProcessRepository.save(process);
        triggerAsyncAfterCommit(saved.getId());

        return enlistmentMapper.toAcceptedResponse(saved);
    }

    /**
     * Retrieves one enlistment process status by id.
     *
     * @param processId process identifier
     * @return process status response
     */
    @Override
    @Transactional(readOnly = true)
    public EnlistmentStatusResponse getStatus(UUID processId) {
        EnlistmentProcess process = enlistmentProcessRepository.findById(processId)
                .orElseThrow(() -> new NotFoundException(AppMessages.ENLISTMENT_PROCESS_NOT_FOUND_WITH_ID_MESSAGE + processId));

        return enlistmentMapper.toStatusResponse(process);
    }

    /**
     * Returns all enlistment processes in a paged response.
     *
     * @param pageable pagination configuration
     * @return paged process status responses
     */
    @Override
    @Transactional(readOnly = true)
    public Page<EnlistmentStatusResponse> listProcesses(Pageable pageable) {
        return enlistmentProcessRepository.findAll(pageable)
                .map(enlistmentMapper::toStatusResponse);
    }

    /**
     * Triggers async processing after transaction commit when available.
     *
     * @param processId enlistment process id
     */
    private void triggerAsyncAfterCommit(UUID processId) {
        // Synchronization Transactional its active in this thread
        // if it false we can't register a callbacks
        if (!TransactionSynchronizationManager.isSynchronizationActive()) {
            enlistmentAsyncWorker.process(processId);
            return;
        }

        // using afterCommit because we want to start enlistment when petition its saved in database
        // if service don't save we don't want to start enlistment
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            /**
             * Starts async processing once the current transaction has been committed.
             */
            public void afterCommit() {
                enlistmentAsyncWorker.process(processId);
            }
        });
    }
}
