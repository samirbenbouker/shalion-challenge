package com.shalion.challenge.service.impl;

import com.shalion.challenge.domain.School;
import com.shalion.challenge.domain.Student;
import com.shalion.challenge.domain.EnlistmentProcess;
import com.shalion.challenge.domain.EnlistmentStatus;
import com.shalion.challenge.repository.EnlistmentProcessRepository;
import com.shalion.challenge.repository.SchoolRepository;
import com.shalion.challenge.repository.StudentRepository;
import com.shalion.challenge.util.AppMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.UUID;

@Service
public class EnlistmentAsyncWorker {

    private static final Integer START_RANDOM_DELAY = 2500;
    private static final Integer END_RANDOM_DELAY = 10000;

    @Autowired
    private EnlistmentProcessRepository enlistmentProcessRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private SchoolRepository schoolRepository;

    @Async
    @Transactional
    public void process(UUID processId) {
        Optional<EnlistmentProcess> maybeProcess = enlistmentProcessRepository.findById(processId);
        if (maybeProcess.isEmpty()) {
            return;
        }

        EnlistmentProcess process = maybeProcess.get();
        inProgress(process);
        randomDelay();

        Optional<Student> maybeStudent = studentRepository.findById(process.getStudentId());
        if (maybeStudent.isEmpty()) {
            fail(process, AppMessages.STUDENT_NOT_FOUND_WITH_ID_MESSAGE + process.getStudentId());
            return;
        }

        Optional<School> maybeSchool = schoolRepository.findById(process.getSchoolId());
        if (maybeSchool.isEmpty()) {
            fail(process, AppMessages.SCHOOL_NOT_FOUND_WITH_ID_MESSAGE + process.getSchoolId());
            return;
        }

        Student student = maybeStudent.get();
        School school = maybeSchool.get();

        if (student.getSchool() != null && school.getId().equals(student.getSchool().getId())) {
            fail(process, AppMessages.STUDENT_IS_ALREADY_ENLISTED_IN_THIS_SCHOOL_MESSAGE);
            return;
        }

        long currentCount = studentRepository.countBySchoolId(school.getId());
        if (currentCount >= school.getMaxCapacity()) {
            fail(process, AppMessages.SCHOOL_HAS_NO_AVAILABLE_CAPACITY_MESSAGE);
            return;
        }

        student.setSchool(school);
        studentRepository.save(student);
        succeed(process, AppMessages.ENLISTMENT_COMPLETED_SUCCESSFULLY_MESSAGE);
    }

    private void inProgress(EnlistmentProcess process) {
        process.setStatus(EnlistmentStatus.IN_PROGRESS);
        process.setMessage(AppMessages.ENLISTMENT_IN_PROGRESS_MESSAGE);
        enlistmentProcessRepository.save(process);
    }

    private void succeed(EnlistmentProcess process, String message) {
        process.setStatus(EnlistmentStatus.SUCCESS);
        process.setFinished(true);
        process.setSuccess(true);
        process.setMessage(message);
        process.setCompletedAt(Instant.now());
        enlistmentProcessRepository.save(process);
    }

    private void fail(EnlistmentProcess process, String message) {
        process.setStatus(EnlistmentStatus.FAILED);
        process.setFinished(true);
        process.setSuccess(false);
        process.setMessage(message);
        process.setCompletedAt(Instant.now());
        enlistmentProcessRepository.save(process);
    }

    private void randomDelay() {
        int millis = ThreadLocalRandom.current().nextInt(START_RANDOM_DELAY, END_RANDOM_DELAY);
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
