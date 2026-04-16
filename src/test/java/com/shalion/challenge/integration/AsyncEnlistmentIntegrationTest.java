package com.shalion.challenge.integration;

import com.shalion.challenge.dto.*;
import com.shalion.challenge.domain.EnlistmentStatus;
import com.shalion.challenge.service.EnlistmentService;
import com.shalion.challenge.service.SchoolService;
import com.shalion.challenge.service.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class AsyncEnlistmentIntegrationTest {

    @Autowired
    private EnlistmentService enlistmentService;

    @Autowired
    private SchoolService schoolService;

    @Autowired
    private StudentService studentService;

    @Test
    void asyncEnlistmentEventuallyFinishesAndMovesStudent() throws Exception {
        SchoolResponse origin = schoolService.create(new SchoolRequest("Async Origin", 100));
        SchoolResponse target = schoolService.create(new SchoolRequest("Async Target", 100));
        StudentResponse student = studentService.create(new StudentRequest("Async Student", origin.id()));

        EnlistmentAcceptedResponse accepted = enlistmentService.startEnlistment(new EnlistmentRequest(student.id(), target.id()));
        assertThat(accepted.status()).isEqualTo(EnlistmentStatus.PENDING);

        EnlistmentStatusResponse status = null;
        Instant timeout = Instant.now().plus(Duration.ofSeconds(10));
        while (Instant.now().isBefore(timeout)) {
            status = enlistmentService.getStatus(accepted.processId());
            if (status.finished()) {
                break;
            }
            Thread.sleep(200);
        }

        assertThat(status).isNotNull();
        assertThat(status.finished()).isTrue();
        assertThat(status.success()).isTrue();

        StudentResponse updated = studentService.getById(student.id());
        assertThat(updated.schoolId()).isEqualTo(target.id());
    }
}
