package com.shalion.challenge.repository;

import com.shalion.challenge.domain.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {

    long countBySchoolId(Long schoolId);
    void deleteBySchoolId(Long schoolId);
    Page<Student> findBySchoolIdAndNameContainingIgnoreCase(Long schoolId, String name, Pageable pageable);

}
