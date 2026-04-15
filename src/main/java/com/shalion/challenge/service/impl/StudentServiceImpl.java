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

    @Override
    @Transactional
    public StudentResponse update(Long id, StudentRequest request) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Student not found with id " + id));

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

    @Override
    @Transactional
    public void delete(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Student not found with id " + id));
        studentRepository.delete(student);
    }

    @Override
    @Transactional(readOnly = true)
    public StudentResponse getById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Student not found with id " + id));
        return studentMapper.toResponse(student);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StudentResponse> listBySchool(Long schoolId, String name, Pageable pageable) {
        schoolService.findEntityById(schoolId);
        return studentRepository.findBySchoolIdAndNameContainingIgnoreCase(schoolId, name == null ? "" : name, pageable)
                .map(studentMapper::toResponse);
    }

    private void ensureCapacityAvailable(Long schoolId, Integer maxCapacity) {
        long currentCount = studentRepository.countBySchoolId(schoolId);
        if (currentCount >= maxCapacity) {
            throw new ConflictException("School has no available capacity");
        }
    }
}
