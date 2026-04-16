package com.shalion.challenge.controller.impl;

import com.shalion.challenge.dto.StudentRequest;
import com.shalion.challenge.dto.StudentResponse;
import com.shalion.challenge.service.StudentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentControllerImplTest {

    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentControllerImpl controller;

    @Test
    void createDelegatesToService() {
        StudentRequest request = new StudentRequest("Ana", 1L);
        StudentResponse response = new StudentResponse(10L, "Ana", 1L, "School");
        when(studentService.create(request)).thenReturn(response);

        StudentResponse result = controller.create(request);

        assertThat(result).isEqualTo(response);
        verify(studentService).create(request);
    }

    @Test
    void getByIdDelegatesToService() {
        StudentResponse response = new StudentResponse(10L, "Ana", 1L, "School");
        when(studentService.getById(10L)).thenReturn(response);

        StudentResponse result = controller.getById(10L);

        assertThat(result).isEqualTo(response);
        verify(studentService).getById(10L);
    }
}
