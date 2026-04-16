package com.shalion.challenge.service.impl;

import com.shalion.challenge.domain.EnlistmentProcess;
import com.shalion.challenge.domain.EnlistmentStatus;
import com.shalion.challenge.dto.EnlistmentAcceptedResponse;
import com.shalion.challenge.dto.EnlistmentRequest;
import com.shalion.challenge.dto.EnlistmentStatusResponse;
import com.shalion.challenge.exception.NotFoundException;
import com.shalion.challenge.mapper.EnlistmentMapper;
import com.shalion.challenge.repository.EnlistmentProcessRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EnlistmentServiceImplTest {

    @Mock
    private EnlistmentProcessRepository enlistmentProcessRepository;

    @Mock
    private EnlistmentAsyncWorker enlistmentAsyncWorker;

    @Mock
    private EnlistmentMapper enlistmentMapper;

    @InjectMocks
    private EnlistmentServiceImpl service;

    @Test
    void startEnlistmentCreatesPendingProcessAndTriggersWorker() {
        UUID id = UUID.randomUUID();
        EnlistmentProcess saved = new EnlistmentProcess();
        saved.setId(id);
        saved.setStudentId(1L);
        saved.setSchoolId(2L);
        saved.setStatus(EnlistmentStatus.PENDING);
        saved.setFinished(false);

        EnlistmentAcceptedResponse mapped = new EnlistmentAcceptedResponse(id, EnlistmentStatus.PENDING);
        when(enlistmentProcessRepository.save(org.mockito.ArgumentMatchers.any(EnlistmentProcess.class))).thenReturn(saved);
        when(enlistmentMapper.toAcceptedResponse(saved)).thenReturn(mapped);

        EnlistmentAcceptedResponse result = service.startEnlistment(new EnlistmentRequest(1L, 2L));

        ArgumentCaptor<EnlistmentProcess> captor = ArgumentCaptor.forClass(EnlistmentProcess.class);
        verify(enlistmentProcessRepository).save(captor.capture());
        EnlistmentProcess process = captor.getValue();
        assertThat(process.getStatus()).isEqualTo(EnlistmentStatus.PENDING);
        assertThat(process.getFinished()).isFalse();

        verify(enlistmentAsyncWorker).process(id);
        assertThat(result).isEqualTo(mapped);
    }

    @Test
    void getStatusThrowsWhenProcessNotFound() {
        UUID id = UUID.randomUUID();
        when(enlistmentProcessRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getStatus(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Enlistment process not found");
    }

    @Test
    void getStatusReturnsMappedResponse() {
        UUID id = UUID.randomUUID();
        EnlistmentProcess process = new EnlistmentProcess();
        process.setId(id);
        process.setStudentId(1L);
        process.setSchoolId(2L);
        process.setStatus(EnlistmentStatus.SUCCESS);
        process.setFinished(true);
        process.setSuccess(true);
        process.setMessage("ok");
        process.setCreatedAt(Instant.now());
        process.setCompletedAt(Instant.now());

        EnlistmentStatusResponse mapped = new EnlistmentStatusResponse(id, 1L, 2L, EnlistmentStatus.SUCCESS, true, true,
                "ok", process.getCreatedAt(), process.getCompletedAt());

        when(enlistmentProcessRepository.findById(id)).thenReturn(Optional.of(process));
        when(enlistmentMapper.toStatusResponse(process)).thenReturn(mapped);

        EnlistmentStatusResponse result = service.getStatus(id);

        assertThat(result).isEqualTo(mapped);
    }

    @Test
    void listProcessesReturnsMappedPage() {
        EnlistmentProcess process = new EnlistmentProcess();
        process.setId(UUID.randomUUID());
        process.setStudentId(1L);
        process.setSchoolId(2L);
        process.setStatus(EnlistmentStatus.IN_PROGRESS);
        process.setFinished(false);

        PageRequest pageable = PageRequest.of(0, 5);
        Page<EnlistmentProcess> page = new PageImpl<>(List.of(process), pageable, 1);
        EnlistmentStatusResponse mapped = new EnlistmentStatusResponse(
                process.getId(), 1L, 2L, EnlistmentStatus.IN_PROGRESS, false, null, "progress", Instant.now(), null
        );
        when(enlistmentProcessRepository.findAll(pageable)).thenReturn(page);
        when(enlistmentMapper.toStatusResponse(process)).thenReturn(mapped);

        Page<EnlistmentStatusResponse> result = service.listProcesses(pageable);

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0)).isEqualTo(mapped);
    }
}
