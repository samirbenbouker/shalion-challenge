package com.shalion.challenge.service.impl;

import com.shalion.challenge.domain.School;
import com.shalion.challenge.domain.Student;
import com.shalion.challenge.domain.EnlistmentProcess;
import com.shalion.challenge.domain.EnlistmentStatus;
import com.shalion.challenge.repository.EnlistmentProcessRepository;
import com.shalion.challenge.repository.SchoolRepository;
import com.shalion.challenge.repository.StudentRepository;
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
            fail(process, "Student not found with id " + process.getStudentId());
            return;
        }

        Optional<School> maybeSchool = schoolRepository.findById(process.getSchoolId());
        if (maybeSchool.isEmpty()) {
            fail(process, "School not found with id " + process.getSchoolId());
            return;
        }

        Student student = maybeStudent.get();
        School school = maybeSchool.get();

        if (student.getSchool() != null && school.getId().equals(student.getSchool().getId())) {
            fail(process, "Student is already enlisted in this school");
            return;
        }

        long currentCount = studentRepository.countBySchoolId(school.getId());
        if (currentCount >= school.getMaxCapacity()) {
            fail(process, "School has no available capacity");
            return;
        }

        student.setSchool(school);
        studentRepository.save(student);
        succeed(process, "Enlistment completed successfully");
    }

    private void inProgress(EnlistmentProcess process) {
        process.setStatus(EnlistmentStatus.IN_PROGRESS);
        process.setMessage("Enlistment in progress");
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
        int millis = ThreadLocalRandom.current().nextInt(2500, 10000);
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
