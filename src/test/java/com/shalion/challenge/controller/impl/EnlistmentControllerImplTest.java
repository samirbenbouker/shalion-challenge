package com.shalion.challenge.controller.impl;

import com.shalion.challenge.domain.EnlistmentStatus;
import com.shalion.challenge.dto.EnlistmentAcceptedResponse;
import com.shalion.challenge.dto.EnlistmentRequest;
import com.shalion.challenge.dto.EnlistmentStatusResponse;
import com.shalion.challenge.service.EnlistmentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EnlistmentControllerImplTest {

    @Mock
    private EnlistmentService enlistmentService;

    @InjectMocks
    private EnlistmentControllerImpl controller;

    @Test
    void startEnlistmentDelegatesToService() {
        EnlistmentRequest request = new EnlistmentRequest(1L, 2L);
        EnlistmentAcceptedResponse response = new EnlistmentAcceptedResponse(UUID.randomUUID(), EnlistmentStatus.PENDING);
        when(enlistmentService.startEnlistment(request)).thenReturn(response);

        EnlistmentAcceptedResponse result = controller.startEnlistment(request);

        assertThat(result).isEqualTo(response);
        verify(enlistmentService).startEnlistment(request);
    }

    @Test
    void getStatusDelegatesToService() {
        UUID processId = UUID.randomUUID();
        EnlistmentStatusResponse response = new EnlistmentStatusResponse(processId, 1L, 2L, EnlistmentStatus.SUCCESS, true, true,
                "ok", Instant.now(), Instant.now());
        when(enlistmentService.getStatus(processId)).thenReturn(response);

        EnlistmentStatusResponse result = controller.getStatus(processId);

        assertThat(result).isEqualTo(response);
        verify(enlistmentService).getStatus(processId);
    }

    @Test
    void listProcessesBuildsPageableAndDelegates() {
        EnlistmentStatusResponse item = new EnlistmentStatusResponse(
                UUID.randomUUID(), 1L, 2L, EnlistmentStatus.PENDING, false, null, "pending", Instant.now(), null
        );
        Page<EnlistmentStatusResponse> page = new PageImpl<>(List.of(item));
        when(enlistmentService.listProcesses(any(Pageable.class))).thenReturn(page);

        Page<EnlistmentStatusResponse> result = controller.listProcesses(0, 10, "createdAt,desc");

        assertThat(result).isEqualTo(page);
        verify(enlistmentService).listProcesses(any(Pageable.class));
    }
}
