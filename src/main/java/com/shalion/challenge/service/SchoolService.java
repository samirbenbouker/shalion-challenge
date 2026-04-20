package com.shalion.challenge.service;

import com.shalion.challenge.domain.School;
import com.shalion.challenge.dto.SchoolRequest;
import com.shalion.challenge.dto.SchoolResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SchoolService {

    /**
     * Creates a school after business validation.
     *
     * @param request school creation payload
     * @return created school response
     */
    SchoolResponse create(SchoolRequest request);

    /**
     * Updates a school after business validation.
     *
     * @param id school identifier
     * @param request school update payload
     * @return updated school response
     */
    SchoolResponse update(Long id, SchoolRequest request);

    /**
     * Deletes a school by identifier.
     *
     * @param id school identifier
     */
    void delete(Long id);

    /**
     * Returns paged schools filtered by partial name.
     *
     * @param name partial name filter
     * @param pageable pagination configuration
     * @return paged school responses
     */
    Page<SchoolResponse> list(String name, Pageable pageable);

    /**
     * Returns detailed school information including students.
     *
     * @param id school identifier
     * @return school detail response
     */
    SchoolResponse getById(Long id);

    /**
     * Resolves a school entity or throws if it does not exist.
     *
     * @param id school identifier
     * @return school entity
     */
    School findEntityById(Long id);

}
