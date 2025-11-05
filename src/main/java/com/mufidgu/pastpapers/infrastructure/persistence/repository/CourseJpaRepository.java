package com.mufidgu.pastpapers.infrastructure.persistence.repository;

import com.mufidgu.pastpapers.infrastructure.persistence.entity.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA repository for CourseEntity.
 */
@Repository
public interface CourseJpaRepository extends JpaRepository<CourseEntity, UUID> {

    Optional<CourseEntity> findByShortNameAndFullName(String shortName, String fullName);
}
