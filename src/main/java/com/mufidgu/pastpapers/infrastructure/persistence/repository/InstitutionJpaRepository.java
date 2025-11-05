package com.mufidgu.pastpapers.infrastructure.persistence.repository;

import com.mufidgu.pastpapers.infrastructure.persistence.entity.InstitutionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA repository for InstitutionEntity.
 */
@Repository
public interface InstitutionJpaRepository extends JpaRepository<InstitutionEntity, UUID> {

    Optional<InstitutionEntity> findByShortNameAndFullName(String shortName, String fullName);
}
