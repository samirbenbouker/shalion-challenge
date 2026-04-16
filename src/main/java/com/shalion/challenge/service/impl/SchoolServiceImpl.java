package com.shalion.challenge.service.impl;

import com.shalion.challenge.domain.School;
import com.shalion.challenge.dto.SchoolRequest;
import com.shalion.challenge.dto.SchoolResponse;
import com.shalion.challenge.exception.ConflictException;
import com.shalion.challenge.exception.NotFoundException;
import com.shalion.challenge.mapper.SchoolMapper;
import com.shalion.challenge.repository.SchoolRepository;
import com.shalion.challenge.service.SchoolService;
import com.shalion.challenge.util.AppMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SchoolServiceImpl implements SchoolService {

    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private SchoolMapper schoolMapper;

    @Override
    @Transactional
    public SchoolResponse create(SchoolRequest request) {
        String normalizedName = request.name().trim();
        if (schoolRepository.existsByNameIgnoreCase(normalizedName)) {
            throw new ConflictException(AppMessages.SCHOOL_NAME_ALREADY_EXISTS_MESSAGE);
        }

        School school = schoolMapper.toEntity(request);
        school.setName(normalizedName);

        School saved = schoolRepository.save(school);
        return schoolMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public SchoolResponse update(Long id, SchoolRequest request) {
        School school = findEntityById(id);

        String normalizedName = request.name().trim();
        if (schoolRepository.existsByNameIgnoreCaseAndIdNot(normalizedName, id)) {
            throw new ConflictException(AppMessages.SCHOOL_NAME_ALREADY_EXISTS_MESSAGE);
        }
        if (request.maxCapacity() < school.getStudents().size()) {
            throw new ConflictException(AppMessages.SCHOOL_MAX_CAPACITY_BE_LOWER_MESSAGE);
        }

        schoolMapper.updateEntityFromDto(request, school);
        school.setName(normalizedName);

        School updated = schoolRepository.save(school);
        return schoolMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        School school = findEntityById(id);
        schoolRepository.delete(school);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SchoolResponse> list(String name, Pageable pageable) {
        return schoolRepository.findByNameContainingIgnoreCase(name == null ? AppMessages.EMPTY_STRING : name, pageable)
                .map(schoolMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public SchoolResponse getById(Long id) {
        School school = findEntityById(id);
        return schoolMapper.toDetailResponse(school);
    }

    @Override
    @Transactional(readOnly = true)
    public School findEntityById(Long id) {
        return schoolRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(AppMessages.SCHOOL_NOT_FOUND_WITH_ID_MESSAGE + id));
    }
}
