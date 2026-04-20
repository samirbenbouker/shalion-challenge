package com.shalion.challenge.controller;

import com.shalion.challenge.dto.SchoolRequest;
import com.shalion.challenge.dto.SchoolResponse;
import org.springframework.data.domain.Page;

public interface SchoolController {

    /**
     * Creates a school.
     *
     * @param request school creation payload
     * @return created school
     */
    SchoolResponse create(SchoolRequest request);

    /**
     * Updates an existing school.
     *
     * @param id school identifier
     * @param request school update payload
     * @return updated school
     */
    SchoolResponse update(Long id, SchoolRequest request);

    /**
     * Deletes an existing school.
     *
     * @param id school identifier
     */
    void delete(Long id);

    /**
     * Searches schools by name with pagination.
     *
     * @param name partial school name
     * @param page zero-based page index
     * @param size page size
     * @param sort sort expression
     * @return paged schools
     */
    Page<SchoolResponse> list(String name, int page, int size, String sort);

    /**
     * Returns a school with enrolled students.
     *
     * @param id school identifier
     * @return school details
     */
    SchoolResponse getById(Long id);

}
