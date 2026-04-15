package com.shalion.challenge.service;

import com.shalion.challenge.domain.School;
import com.shalion.challenge.dto.SchoolRequest;
import com.shalion.challenge.dto.SchoolResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SchoolService {

    SchoolResponse create(SchoolRequest request);
    SchoolResponse update(Long id, SchoolRequest request);
    void delete(Long id);
    Page<SchoolResponse> list(String name, Pageable pageable);
    SchoolResponse getById(Long id);
    School findEntityById(Long id);

}
