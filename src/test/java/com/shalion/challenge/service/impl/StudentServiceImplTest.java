package com.shalion.challenge.service.impl;

import com.shalion.challenge.domain.School;
import com.shalion.challenge.domain.Student;
import com.shalion.challenge.dto.StudentRequest;
import com.shalion.challenge.dto.StudentResponse;
import com.shalion.challenge.exception.ConflictException;
import com.shalion.challenge.exception.NotFoundException;
import com.shalion.challenge.mapper.StudentMapper;
import com.shalion.challenge.repository.StudentRepository;
import com.shalion.challenge.service.SchoolService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceImplTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private SchoolService schoolService;

    @Mock
    private StudentMapper studentMapper;

    @InjectMocks
    private StudentServiceImpl service;

    @Test
    void createThrowsConflictWhenSchoolFull() {
        School school = new School();
        school.setId(1L);
        school.setMaxCapacity(1);

        when(schoolService.findEntityById(1L)).thenReturn(school);
        when(studentRepository.countBySchoolId(1L)).thenReturn(1L);

        assertThatThrownBy(() -> service.create(new StudentRequest("A", 1L)))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("capacity");
    }

    @Test
    void createSavesTrimmedName() {
        School school = new School();
        school.setId(1L);
        school.setMaxCapacity(10);
        Student entity = new Student();
        Student saved = new Student();
        saved.setId(2L);
        saved.setName("Ana");
        saved.setSchool(school);
        StudentResponse response = new StudentResponse(2L, "Ana", 1L, "S");

        when(schoolService.findEntityById(1L)).thenReturn(school);
        when(studentRepository.countBySchoolId(1L)).thenReturn(0L);
        when(studentMapper.toEntity(new StudentRequest("  Ana  ", 1L))).thenReturn(entity);
        when(studentRepository.save(entity)).thenReturn(saved);
        when(studentMapper.toResponse(saved)).thenReturn(response);

        StudentResponse result = service.create(new StudentRequest("  Ana  ", 1L));

        assertThat(entity.getName()).isEqualTo("Ana");
        assertThat(result).isEqualTo(response);
    }

    @Test
    void getByIdThrowsWhenMissing() {
        when(studentRepository.findById(9L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(9L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("9");
    }

    @Test
    void listBySchoolDelegatesWithFallbackName() {
        School school = new School();
        school.setId(1L);
        when(schoolService.findEntityById(1L)).thenReturn(school);
        Page<Student> page = new PageImpl<>(List.of());
        when(studentRepository.findBySchoolIdAndNameContainingIgnoreCase(1L, "", PageRequest.of(0, 10))).thenReturn(page);

        Page<StudentResponse> result = service.listBySchool(1L, null, PageRequest.of(0, 10));

        assertThat(result.getTotalElements()).isEqualTo(0);
        verify(studentRepository).findBySchoolIdAndNameContainingIgnoreCase(1L, "", PageRequest.of(0, 10));
    }
}
