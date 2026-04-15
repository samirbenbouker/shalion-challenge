package com.shalion.challenge.controller.impl;

import com.shalion.challenge.controller.SchoolController;
import com.shalion.challenge.dto.SchoolRequest;
import com.shalion.challenge.dto.SchoolResponse;
import com.shalion.challenge.service.SchoolService;
import com.shalion.challenge.util.PaginationUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/schools")
public class SchoolControllerImpl implements SchoolController {

    @Autowired
    private SchoolService schoolService;

    @Override
    @Operation(summary = "Create school")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SchoolResponse create(@Valid @RequestBody SchoolRequest request) {
        return schoolService.create(request);
    }

    @Override
    @Operation(summary = "Update school")
    @PutMapping("/{id}")
    public SchoolResponse update(@PathVariable Long id, @Valid @RequestBody SchoolRequest request) {
        return schoolService.update(id, request);
    }

    @Override
    @Operation(summary = "Delete school")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        schoolService.delete(id);
    }

    @Override
    @Operation(summary = "Search schools by name (case-insensitive) with pagination")
    @GetMapping
    public Page<SchoolResponse> list(
            @RequestParam(required = false, defaultValue = "") String name,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "20") int size,
            @Parameter(description = "Sort format: field,direction", example = "name,asc")
            @RequestParam(required = false, defaultValue = "id,asc") String sort
    ) {
        Pageable pageable = PaginationUtils.toPageable(page, size, sort, "id");
        return schoolService.list(name, pageable);
    }

    @Override
    @Operation(summary = "Get school detail including students")
    @GetMapping("/{id}")
    public SchoolResponse getById(@PathVariable Long id) {
        return schoolService.getById(id);
    }
}
