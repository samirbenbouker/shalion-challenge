package com.shalion.challenge.mapper;

import com.shalion.challenge.domain.School;
import com.shalion.challenge.domain.Student;
import com.shalion.challenge.dto.EnlistmentAcceptedResponse;
import com.shalion.challenge.dto.EnlistmentStatusResponse;
import com.shalion.challenge.dto.SchoolRequest;
import com.shalion.challenge.dto.SchoolResponse;
import com.shalion.challenge.dto.StudentRequest;
import com.shalion.challenge.dto.StudentResponse;
import com.shalion.challenge.domain.EnlistmentProcess;
import com.shalion.challenge.domain.EnlistmentStatus;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MapperTest {

    private final SchoolMapper schoolMapper = Mappers.getMapper(SchoolMapper.class);
    private final StudentMapper studentMapper = Mappers.getMapper(StudentMapper.class);
    private final EnlistmentMapper enlistmentMapper = Mappers.getMapper(EnlistmentMapper.class);

    @Test
    void schoolMapperMapsRequestToEntityAndToResponse() {
        SchoolRequest request = new SchoolRequest("School", 100);
        School entity = schoolMapper.toEntity(request);

        assertThat(entity.getId()).isNull();
        assertThat(entity.getName()).isEqualTo("School");
        assertThat(entity.getMaxCapacity()).isEqualTo(100);

        SchoolResponse response = schoolMapper.toResponse(entity);
        assertThat(response.students()).isEmpty();
    }

    @Test
    void schoolMapperDetailResponseSortsStudentsById() {
        School school = new School();
        school.setId(1L);
        school.setName("S");
        school.setMaxCapacity(100);

        Student s2 = new Student();
        s2.setId(2L);
        s2.setName("B");
        Student s1 = new Student();
        s1.setId(1L);
        s1.setName("A");
        school.setStudents(List.of(s2, s1));

        SchoolResponse detail = schoolMapper.toDetailResponse(school);

        assertThat(detail.students()).hasSize(2);
        assertThat(detail.students().get(0).id()).isEqualTo(1L);
        assertThat(detail.students().get(1).id()).isEqualTo(2L);
    }

    @Test
    void studentMapperMapsEntityToResponse() {
        School school = new School();
        school.setId(7L);
        school.setName("S");

        Student student = studentMapper.toEntity(new StudentRequest("John", 7L));
        student.setId(3L);
        student.setSchool(school);

        StudentResponse response = studentMapper.toResponse(student);

        assertThat(response.id()).isEqualTo(3L);
        assertThat(response.schoolId()).isEqualTo(7L);
        assertThat(response.schoolName()).isEqualTo("S");
    }

    @Test
    void enlistmentMapperMapsToAcceptedAndStatusResponses() {
        EnlistmentProcess process = new EnlistmentProcess();
        process.setId(java.util.UUID.randomUUID());
        process.setStudentId(1L);
        process.setSchoolId(2L);
        process.setStatus(EnlistmentStatus.SUCCESS);
        process.setFinished(true);
        process.setSuccess(true);
        process.setMessage("ok");
        process.setCreatedAt(Instant.now());
        process.setCompletedAt(Instant.now());

        EnlistmentAcceptedResponse accepted = enlistmentMapper.toAcceptedResponse(process);
        EnlistmentStatusResponse status = enlistmentMapper.toStatusResponse(process);

        assertThat(accepted.processId()).isEqualTo(process.getId());
        assertThat(status.processId()).isEqualTo(process.getId());
        assertThat(status.success()).isTrue();
    }
}
