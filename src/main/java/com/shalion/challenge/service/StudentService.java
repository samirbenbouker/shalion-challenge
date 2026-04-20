package com.shalion.challenge.service;

import com.shalion.challenge.dto.StudentRequest;
import com.shalion.challenge.dto.StudentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StudentService {

    /**
     * Creates a student after business validation.
     *
     * @param request student creation payload
     * @return created student response
     */
    StudentResponse create(StudentRequest request);

    /**
     * Updates an existing student.
     *
     * @param id student identifier
     * @param request student update payload
     * @return updated student response
     */
    StudentResponse update(Long id, StudentRequest request);

    /**
     * Deletes a student by identifier.
     *
     * @param id student identifier
     */
    void delete(Long id);

    /**
     * Returns a student detail by identifier.
     *
     * @param id student identifier
     * @return student response
     */
    StudentResponse getById(Long id);

    /**
     * Returns paged students for a school with a name filter.
     *
     * @param schoolId school identifier
     * @param name partial student name
     * @param pageable pagination configuration
     * @return paged student responses
     */
    Page<StudentResponse> listBySchool(Long schoolId, String name, Pageable pageable);

}
