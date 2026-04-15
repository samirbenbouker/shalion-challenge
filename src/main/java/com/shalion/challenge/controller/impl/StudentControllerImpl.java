package com.shalion.challenge.controller.impl;

import com.shalion.challenge.controller.StudentController;
import com.shalion.challenge.dto.StudentRequest;
import com.shalion.challenge.dto.StudentResponse;
import com.shalion.challenge.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/students")
public class StudentControllerImpl implements StudentController {

    @Autowired
    private StudentService studentService;

    @Override
    @Operation(summary = "Create student")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StudentResponse create(@Valid @RequestBody StudentRequest request) {
        return studentService.create(request);
    }

    @Override
    @Operation(summary = "Update student")
    @PutMapping("/{id}")
    public StudentResponse update(@PathVariable Long id, @Valid @RequestBody StudentRequest request) {
        return studentService.update(id, request);
    }

    @Override
    @Operation(summary = "Delete student")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        studentService.delete(id);
    }

    @Override
    @Operation(summary = "Get student detail")
    @GetMapping("/{id}")
    public StudentResponse getById(@PathVariable Long id) {
        return studentService.getById(id);
    }
}
