package com.shalion.challenge.controller;

import com.shalion.challenge.dto.SchoolRequest;
import com.shalion.challenge.dto.SchoolResponse;
import org.springframework.data.domain.Page;

public interface SchoolController {

    SchoolResponse create(SchoolRequest request);
    SchoolResponse update(Long id, SchoolRequest request);
    void delete(Long id);
    Page<SchoolResponse> list(String name, int page, int size, String sort);
    SchoolResponse getById(Long id);

}
