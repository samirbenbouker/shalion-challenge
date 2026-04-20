package com.shalion.challenge.repository;

import com.shalion.challenge.domain.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {

    /**
     * Counts students currently assigned to a school.
     *
     * @param schoolId school identifier
     * @return number of students in the school
     */
    long countBySchoolId(Long schoolId);

    /**
     * Searches students by exact school id and partial name, case-insensitive.
     *
     * @param schoolId school identifier
     * @param name partial student name
     * @param pageable pagination configuration
     * @return paged students
     */
    Page<Student> findBySchoolIdAndNameContainingIgnoreCase(Long schoolId, String name, Pageable pageable);

}
