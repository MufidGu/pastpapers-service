package com.mufidgu.pastpapers.infrastructure.persistence.repository;

import com.mufidgu.pastpapers.infrastructure.persistence.entity.DegreeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA repository for DegreeEntity.
 */
@Repository
public interface DegreeJpaRepository extends JpaRepository<DegreeEntity, UUID> {

    Optional<DegreeEntity> findByShortNameAndFullName(String shortName, String fullName);
}
