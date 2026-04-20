package com.shalion.challenge.repository;

import com.shalion.challenge.domain.School;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolRepository extends JpaRepository<School, Long> {

    /**
     * Checks if a school exists with the provided name ignoring case.
     *
     * @param name school name
     * @return true when a school already exists
     */
    boolean existsByNameIgnoreCase(String name);

    /**
     * Checks if another school (excluding the given id) exists with the same name.
     *
     * @param name school name
     * @param id excluded school id
     * @return true when a conflicting school exists
     */
    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);

    /**
     * Finds schools by partial name, case-insensitive.
     *
     * @param name partial name
     * @param pageable pagination configuration
     * @return paged schools
     */
    Page<School> findByNameContainingIgnoreCase(String name, Pageable pageable);

}
