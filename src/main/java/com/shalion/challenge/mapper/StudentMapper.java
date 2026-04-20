package com.shalion.challenge.mapper;

import com.shalion.challenge.domain.Student;
import com.shalion.challenge.dto.StudentRequest;
import com.shalion.challenge.dto.StudentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    /**
     * Maps a student request DTO to a new student entity.
     *
     * @param request student request
     * @return student entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "school", ignore = true)
    Student toEntity(StudentRequest request);

    /**
     * Updates an existing student entity from a request DTO.
     *
     * @param request source DTO
     * @param student target entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "school", ignore = true)
    void updateEntityFromDto(StudentRequest request, @MappingTarget Student student);

    /**
     * Maps a student entity to API response format.
     *
     * @param student student entity
     * @return student response
     */
    @Mapping(target = "schoolId", source = "school.id")
    @Mapping(target = "schoolName", source = "school.name")
    StudentResponse toResponse(Student student);
}
