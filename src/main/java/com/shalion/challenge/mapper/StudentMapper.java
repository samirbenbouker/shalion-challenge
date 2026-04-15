package com.shalion.challenge.mapper;

import com.shalion.challenge.domain.Student;
import com.shalion.challenge.dto.StudentRequest;
import com.shalion.challenge.dto.StudentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "school", ignore = true)
    Student toEntity(StudentRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "school", ignore = true)
    void updateEntityFromDto(StudentRequest request, @MappingTarget Student student);

    @Mapping(target = "schoolId", source = "school.id")
    @Mapping(target = "schoolName", source = "school.name")
    StudentResponse toResponse(Student student);
}
