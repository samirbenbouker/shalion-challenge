package com.shalion.challenge.domain;

import com.shalion.challenge.dto.*;
import com.shalion.challenge.exception.ApiError;
import com.shalion.challenge.exception.ConflictException;
import com.shalion.challenge.exception.NotFoundException;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DomainAndDtoTest {

    @Test
    void domainGettersSettersWork() {
        School school = new School();
        school.setId(1L);
        school.setName("S");
        school.setMaxCapacity(100);

        Student student = new Student();
        student.setId(2L);
        student.setName("A");
        student.setSchool(school);

        school.setStudents(List.of(student));

        assertThat(school.getId()).isEqualTo(1L);
        assertThat(school.getStudents()).hasSize(1);
        assertThat(student.getSchool().getName()).isEqualTo("S");
    }

    @Test
    void recordsAndExceptionsWork() {
        SchoolRequest schoolRequest = new SchoolRequest("School", 100);
        StudentRequest studentRequest = new StudentRequest("Ana", 1L);
        StudentSummaryResponse summary = new StudentSummaryResponse(1L, "Ana");
        SchoolResponse schoolResponse = new SchoolResponse(1L, "School", 100, List.of(summary));
        StudentResponse studentResponse = new StudentResponse(2L, "Ana", 1L, "School");
        ApiError apiError = new ApiError(Instant.now(), 400, "Bad Request", "invalid", "/x");

        assertThat(schoolRequest.name()).isEqualTo("School");
        assertThat(studentRequest.schoolId()).isEqualTo(1L);
        assertThat(schoolResponse.students()).hasSize(1);
        assertThat(studentResponse.schoolName()).isEqualTo("School");
        assertThat(apiError.status()).isEqualTo(400);

        assertThat(new ConflictException("c")).hasMessage("c");
        assertThat(new NotFoundException("n")).hasMessage("n");
    }
}
