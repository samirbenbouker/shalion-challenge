package com.shalion.challenge.service.impl;

import com.shalion.challenge.domain.School;
import com.shalion.challenge.dto.SchoolRequest;
import com.shalion.challenge.dto.SchoolResponse;
import com.shalion.challenge.exception.ConflictException;
import com.shalion.challenge.exception.NotFoundException;
import com.shalion.challenge.mapper.SchoolMapper;
import com.shalion.challenge.repository.SchoolRepository;
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
class SchoolServiceImplTest {

    @Mock
    private SchoolRepository schoolRepository;

    @Mock
    private SchoolMapper schoolMapper;

    @InjectMocks
    private SchoolServiceImpl service;

    @Test
    void createThrowsConflictWhenNameExists() {
        SchoolRequest request = new SchoolRequest("  School  ", 100);
        when(schoolRepository.existsByNameIgnoreCase("School")).thenReturn(true);

        assertThatThrownBy(() -> service.create(request))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("already exists");
    }

    @Test
    void createSavesNormalizedName() {
        SchoolRequest request = new SchoolRequest("  School  ", 100);
        School entity = new School();
        School saved = new School();
        saved.setId(1L);
        saved.setName("School");
        saved.setMaxCapacity(100);
        SchoolResponse response = new SchoolResponse(1L, "School", 100);

        when(schoolRepository.existsByNameIgnoreCase("School")).thenReturn(false);
        when(schoolMapper.toEntity(request)).thenReturn(entity);
        when(schoolRepository.save(entity)).thenReturn(saved);
        when(schoolMapper.toResponse(saved)).thenReturn(response);

        SchoolResponse result = service.create(request);

        assertThat(entity.getName()).isEqualTo("School");
        assertThat(result).isEqualTo(response);
    }

    @Test
    void updateThrowsWhenCapacityLowerThanCurrentStudents() {
        School school = new School();
        school.setId(1L);
        school.getStudents().add(new com.shalion.challenge.domain.Student());
        school.getStudents().add(new com.shalion.challenge.domain.Student());

        when(schoolRepository.findById(1L)).thenReturn(Optional.of(school));
        when(schoolRepository.existsByNameIgnoreCaseAndIdNot("S", 1L)).thenReturn(false);

        assertThatThrownBy(() -> service.update(1L, new SchoolRequest("S", 1)))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("maxCapacity");
    }

    @Test
    void listUsesEmptyFilterWhenNameNull() {
        Page<School> page = new PageImpl<>(List.of());
        when(schoolRepository.findByNameContainingIgnoreCase("", PageRequest.of(0, 10))).thenReturn(page);

        Page<SchoolResponse> result = service.list(null, PageRequest.of(0, 10));

        assertThat(result.getTotalElements()).isEqualTo(0);
        verify(schoolRepository).findByNameContainingIgnoreCase("", PageRequest.of(0, 10));
    }

    @Test
    void findEntityByIdThrowsNotFound() {
        when(schoolRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findEntityById(99L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("99");
    }
}
