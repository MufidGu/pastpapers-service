package com.mufidgu.pastpapers.infrastructure.persistence.repository;

import com.mufidgu.pastpapers.infrastructure.persistence.entity.InstructorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA repository for InstructorEntity.
 */
@Repository
public interface InstructorJpaRepository extends JpaRepository<InstructorEntity, UUID> {

    Optional<InstructorEntity> findByFullName(String fullName);
}
