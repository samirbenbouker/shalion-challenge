package com.shalion.challenge.controller.impl;

import com.shalion.challenge.dto.StudentResponse;
import com.shalion.challenge.service.StudentService;
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
class SchoolStudentControllerImplTest {

    @Mock
    private StudentService studentService;

    @InjectMocks
    private SchoolStudentControllerImpl controller;

    @Test
    void listBySchoolBuildsPageableAndDelegates() {
        Page<StudentResponse> page = new PageImpl<>(List.of(new StudentResponse(1L, "Bob", 2L, "School")));
        when(studentService.listBySchool(org.mockito.ArgumentMatchers.eq(2L), org.mockito.ArgumentMatchers.eq("bo"), org.mockito.ArgumentMatchers.any(Pageable.class)))
                .thenReturn(page);

        Page<StudentResponse> result = controller.listBySchool(2L, "bo", 0, 20, "id,asc");

        ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);
        verify(studentService).listBySchool(org.mockito.ArgumentMatchers.eq(2L), org.mockito.ArgumentMatchers.eq("bo"), captor.capture());

        assertThat(result).isEqualTo(page);
        assertThat(captor.getValue().getSort().getOrderFor("id")).isNotNull();
        assertThat(captor.getValue().getSort().getOrderFor("id").isAscending()).isTrue();
    }
}
