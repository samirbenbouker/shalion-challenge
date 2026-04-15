package com.shalion.challenge.service;

import com.shalion.challenge.dto.StudentRequest;
import com.shalion.challenge.dto.StudentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StudentService {

    StudentResponse create(StudentRequest request);
    StudentResponse update(Long id, StudentRequest request);
    void delete(Long id);
    StudentResponse getById(Long id);
    Page<StudentResponse> listBySchool(Long schoolId, String name, Pageable pageable);

}
