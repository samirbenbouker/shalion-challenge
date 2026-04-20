package com.shalion.challenge.service.impl;

import com.shalion.challenge.domain.School;
import com.shalion.challenge.domain.Student;
import com.shalion.challenge.dto.StudentRequest;
import com.shalion.challenge.dto.StudentResponse;
import com.shalion.challenge.exception.ConflictException;
import com.shalion.challenge.exception.NotFoundException;
import com.shalion.challenge.mapper.StudentMapper;
import com.shalion.challenge.repository.StudentRepository;
import com.shalion.challenge.service.SchoolService;
import com.shalion.challenge.service.StudentService;
import com.shalion.challenge.util.AppMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private SchoolService schoolService;

    @Autowired
    private StudentMapper studentMapper;

    /**
     * Creates a student after checking school existence and capacity.
     *
     * @param request student creation payload
     * @return created student response
     */
    @Override
    @Transactional
    public StudentResponse create(StudentRequest request) {
        School school = schoolService.findEntityById(request.schoolId());
        ensureCapacityAvailable(school.getId(), school.getMaxCapacity());

        Student student = studentMapper.toEntity(request);
        student.setName(request.name().trim());
        student.setSchool(school);

        Student saved = studentRepository.save(student);
        return studentMapper.toResponse(saved);
    }

    /**
     * Updates a student and validates capacity when changing schools.
     *
     * @param id student identifier
     * @param request student update payload
     * @return updated student response
     */
    @Override
    @Transactional
    public StudentResponse update(Long id, StudentRequest request) {
        Student student = findEntityById(id);

        School targetSchool = schoolService.findEntityById(request.schoolId());
        boolean schoolChanged = !student.getSchool().getId().equals(targetSchool.getId());
        if (schoolChanged) {
            ensureCapacityAvailable(targetSchool.getId(), targetSchool.getMaxCapacity());
        }

        studentMapper.updateEntityFromDto(request, student);
        student.setName(request.name().trim());
        student.setSchool(targetSchool);

        Student updated = studentRepository.save(student);
        return studentMapper.toResponse(updated);
    }

    /**
     * Deletes a student by id.
     *
     * @param id student identifier
     */
    @Override
    @Transactional
    public void delete(Long id) {
        Student student = findEntityById(id);
        studentRepository.delete(student);
    }

    /**
     * Returns student details by id.
     *
     * @param id student identifier
     * @return student response
     */
    @Override
    @Transactional(readOnly = true)
    public StudentResponse getById(Long id) {
        Student student = findEntityById(id);
        return studentMapper.toResponse(student);
    }

    /**
     * Returns paged students filtered by school and partial name.
     *
     * @param schoolId school identifier
     * @param name partial student name
     * @param pageable pagination configuration
     * @return paged student responses
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StudentResponse> listBySchool(Long schoolId, String name, Pageable pageable) {
        schoolService.findEntityById(schoolId);
        return studentRepository.findBySchoolIdAndNameContainingIgnoreCase(schoolId, name == null ? AppMessages.EMPTY_STRING : name, pageable)
                .map(studentMapper::toResponse);
    }

    /**
     * Finds a student entity by id or throws a not-found error.
     *
     * @param id student identifier
     * @return student entity
     */
    private Student findEntityById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(AppMessages.STUDENT_NOT_FOUND_WITH_ID_MESSAGE + id));
    }

    /**
     * Validates that a school still has capacity for a new student.
     *
     * @param schoolId school identifier
     * @param maxCapacity configured maximum capacity
     */
    private void ensureCapacityAvailable(Long schoolId, Integer maxCapacity) {
        long currentCount = studentRepository.countBySchoolId(schoolId);
        if (currentCount >= maxCapacity) {
            throw new ConflictException(AppMessages.SCHOOL_HAS_NO_AVAILABLE_CAPACITY_MESSAGE);
        }
    }
}
