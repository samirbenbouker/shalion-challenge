package com.shalion.challenge.controller.impl;

import com.shalion.challenge.controller.SchoolStudentController;
import com.shalion.challenge.dto.StudentResponse;
import com.shalion.challenge.service.StudentService;
import com.shalion.challenge.util.PaginationUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/schools")
public class SchoolStudentControllerImpl implements SchoolStudentController {

    @Autowired
    private StudentService studentService;

    @Override
    @Operation(summary = "Search students by schoolId and name (case-insensitive) with pagination")
    @GetMapping("/{schoolId}/students")
    public Page<StudentResponse> listBySchool(
            @PathVariable Long schoolId,
            @RequestParam(required = false, defaultValue = "") String name,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "20") int size,
            @Parameter(description = "Sort format: field,direction", example = "name,asc")
            @RequestParam(required = false, defaultValue = "id,asc") String sort
    ) {
        Pageable pageable = PaginationUtils.toPageable(page, size, sort, "id");
        return studentService.listBySchool(schoolId, name, pageable);
    }
}
