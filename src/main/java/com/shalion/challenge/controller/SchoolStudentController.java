package com.shalion.challenge.controller;

import com.shalion.challenge.dto.StudentResponse;
import org.springframework.data.domain.Page;

public interface SchoolStudentController {

    /**
     * Searches students by school and name with pagination.
     *
     * @param schoolId school identifier
     * @param name partial student name
     * @param page zero-based page index
     * @param size page size
     * @param sort sort expression
     * @return paged students matching the filters
     */
    Page<StudentResponse> listBySchool(Long schoolId, String name, int page, int size, String sort);

}
