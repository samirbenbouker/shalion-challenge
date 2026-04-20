package com.shalion.challenge.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "enlistment_processes")
@Getter
@Setter
public class EnlistmentProcess {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private Long studentId;

    @Column(nullable = false)
    private Long schoolId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EnlistmentStatus status;

    @Column(nullable = false)
    private Boolean finished;

    @Column
    private Boolean success;

    @Column(length = 500)
    private String message;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column
    private Instant completedAt;

    /**
     * Initializes creation timestamp before first persistence.
     */
    @PrePersist
    public void onCreate() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
    }

}
