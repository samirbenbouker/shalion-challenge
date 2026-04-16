package com.shalion.challenge.controller.impl;

import com.shalion.challenge.dto.SchoolRequest;
import com.shalion.challenge.dto.SchoolResponse;
import com.shalion.challenge.service.SchoolService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SchoolControllerImplTest {

    @Mock
    private SchoolService schoolService;

    @InjectMocks
    private SchoolControllerImpl controller;

    @Test
    void createDelegatesToService() {
        SchoolRequest request = new SchoolRequest("School", 100);
        SchoolResponse response = new SchoolResponse(1L, "School", 100);
        when(schoolService.create(request)).thenReturn(response);

        SchoolResponse result = controller.create(request);

        assertThat(result).isEqualTo(response);
        verify(schoolService).create(request);
    }

    @Test
    void listBuildsPageableAndDelegatesToService() {
        Page<SchoolResponse> page = new PageImpl<>(List.of(new SchoolResponse(1L, "A", 100)));
        when(schoolService.list(org.mockito.ArgumentMatchers.eq("abc"), org.mockito.ArgumentMatchers.any(Pageable.class)))
                .thenReturn(page);

        Page<SchoolResponse> result = controller.list("abc", 1, 5, "name,desc");

        ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);
        verify(schoolService).list(org.mockito.ArgumentMatchers.eq("abc"), captor.capture());
        Pageable pageable = captor.getValue();

        assertThat(result).isEqualTo(page);
        assertThat(pageable.getPageNumber()).isEqualTo(1);
        assertThat(pageable.getPageSize()).isEqualTo(5);
        assertThat(pageable.getSort().getOrderFor("name")).isNotNull();
        assertThat(pageable.getSort().getOrderFor("name").isDescending()).isTrue();
    }
}
