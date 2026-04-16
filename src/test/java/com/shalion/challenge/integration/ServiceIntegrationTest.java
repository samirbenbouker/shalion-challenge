package com.shalion.challenge.integration;

import com.shalion.challenge.dto.SchoolRequest;
import com.shalion.challenge.dto.SchoolResponse;
import com.shalion.challenge.dto.StudentRequest;
import com.shalion.challenge.dto.StudentResponse;
import com.shalion.challenge.exception.ConflictException;
import com.shalion.challenge.service.SchoolService;
import com.shalion.challenge.service.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
class ServiceIntegrationTest {

    @Autowired
    private SchoolService schoolService;

    @Autowired
    private StudentService studentService;

    @Test
    void contextLoadsAndServicesAreAvailable() {
        assertThat(schoolService).isNotNull();
        assertThat(studentService).isNotNull();
    }

    @Test
    void schoolAndStudentFlowWorks() {
        SchoolResponse school = schoolService.create(new SchoolRequest("Integration School", 50));
        StudentResponse student = studentService.create(new StudentRequest("Integration Student", school.id()));

        SchoolResponse schoolDetail = schoolService.getById(school.id());
        StudentResponse studentDetail = studentService.getById(student.id());
        Page<StudentResponse> students = studentService.listBySchool(school.id(), "integration", PageRequest.of(0, 10));

        assertThat(schoolDetail.students()).hasSize(1);
        assertThat(studentDetail.schoolId()).isEqualTo(school.id());
        assertThat(students.getTotalElements()).isEqualTo(1);
    }

    @Test
    void capacityRuleIsEnforced() {
        SchoolResponse school = schoolService.create(new SchoolRequest("Capacity School", 50));
        for (int i = 0; i < 50; i++) {
            studentService.create(new StudentRequest("S-" + i, school.id()));
        }

        assertThatThrownBy(() -> studentService.create(new StudentRequest("Overflow", school.id())))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("capacity");
    }
}
