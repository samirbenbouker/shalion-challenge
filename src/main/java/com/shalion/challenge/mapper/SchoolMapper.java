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

    /**
     * Maps a school request DTO to a new school entity.
     *
     * @param request school request
     * @return school entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "students", ignore = true)
    School toEntity(SchoolRequest request);

    /**
     * Updates an existing school entity from a request DTO.
     *
     * @param request source DTO
     * @param school target entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "students", ignore = true)
    void updateEntityFromDto(SchoolRequest request, @MappingTarget School school);

    /**
     * Maps a school entity to a summary response.
     *
     * @param school school entity
     * @return school response without enrolled students
     */
    @Mapping(target = "students", expression = "java(java.util.List.of())")
    SchoolResponse toResponse(School school);

    /**
     * Maps a student entity into school-detail student summary.
     *
     * @param student student entity
     * @return student summary response
     */
    StudentSummaryResponse toStudentSummaryResponse(Student student);

    /**
     * Builds a school detail response including sorted student summaries.
     *
     * @param school school entity
     * @return detailed school response
     */
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
