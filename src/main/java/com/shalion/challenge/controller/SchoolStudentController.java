package com.shalion.challenge.controller;

import com.shalion.challenge.dto.StudentResponse;
import org.springframework.data.domain.Page;

public interface SchoolStudentController {

    Page<StudentResponse> listBySchool(Long schoolId, String name, int page, int size, String sort);

}
