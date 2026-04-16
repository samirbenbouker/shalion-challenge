package com.shalion.challenge.repository;

import com.shalion.challenge.domain.EnlistmentProcess;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EnlistmentProcessRepository extends JpaRepository<EnlistmentProcess, UUID> {
}
