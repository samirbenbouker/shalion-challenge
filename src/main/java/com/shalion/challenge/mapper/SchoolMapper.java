package com.shalion.challenge.mapper;

import java.util.Comparator;
import java.util.List;

import com.shalion.challenge.domain.School;
import com.shalion.challenge.domain.Student;
import com.shalion.challenge.dto.SchoolRequest;
import com.shalion.challenge.dto.SchoolResponse;
import com.shalion.challenge.dto.StudentSummaryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SchoolMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "students", ignore = true)
    School toEntity(SchoolRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "students", ignore = true)
    void updateEntityFromDto(SchoolRequest request, @MappingTarget School school);

    @Mapping(target = "students", expression = "java(java.util.List.of())")
    SchoolResponse toResponse(School school);

    StudentSummaryResponse toStudentSummaryResponse(Student student);

    default SchoolResponse toDetailResponse(School school) {
        List<StudentSummaryResponse> students = school.getStudents().stream()
                .sorted(Comparator.comparing(student -> student.getId() == null ? Long.MAX_VALUE : student.getId()))
                .map(this::toStudentSummaryResponse)
                .toList();

        return new SchoolResponse(
                school.getId(),
                school.getName(),
                school.getMaxCapacity(),
                students
        );
    }
}
