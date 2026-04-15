package com.shalion.challenge.controller;

import com.shalion.challenge.dto.StudentRequest;
import com.shalion.challenge.dto.StudentResponse;

public interface StudentController {

    StudentResponse create(StudentRequest request);
    StudentResponse update(Long id, StudentRequest request);
    void delete(Long id);
    StudentResponse getById(Long id);

}
