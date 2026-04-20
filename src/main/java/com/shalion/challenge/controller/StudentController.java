package com.shalion.challenge.controller;

import com.shalion.challenge.dto.StudentRequest;
import com.shalion.challenge.dto.StudentResponse;

public interface StudentController {

    /**
     * Creates a student.
     *
     * @param request student creation payload
     * @return created student
     */
    StudentResponse create(StudentRequest request);

    /**
     * Updates an existing student.
     *
     * @param id student identifier
     * @param request student update payload
     * @return updated student
     */
    StudentResponse update(Long id, StudentRequest request);

    /**
     * Deletes an existing student.
     *
     * @param id student identifier
     */
    void delete(Long id);

    /**
     * Returns a student by identifier.
     *
     * @param id student identifier
     * @return student details
     */
    StudentResponse getById(Long id);

}
