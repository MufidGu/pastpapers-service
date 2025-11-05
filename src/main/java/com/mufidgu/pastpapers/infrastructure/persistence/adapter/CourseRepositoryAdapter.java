package com.mufidgu.pastpapers.infrastructure.persistence.adapter;

import com.mufidgu.pastpapers.domain.course.Course;
import com.mufidgu.pastpapers.domain.course.spi.Courses;
import com.mufidgu.pastpapers.infrastructure.persistence.entity.CourseEntity;
import com.mufidgu.pastpapers.infrastructure.persistence.entity.DegreeEntity;
import com.mufidgu.pastpapers.infrastructure.persistence.entity.InstitutionEntity;
import com.mufidgu.pastpapers.infrastructure.persistence.repository.CourseJpaRepository;
import com.mufidgu.pastpapers.infrastructure.persistence.repository.DegreeJpaRepository;
import com.mufidgu.pastpapers.infrastructure.persistence.repository.InstitutionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * JPA adapter implementation for the Courses SPI.
 * Handles many-to-many relationships with degrees and institutions.
 * Disabled in test profile to allow stub implementations.
 */
@Repository
@Primary
@Profile("!test")
@RequiredArgsConstructor
@Transactional
public class CourseRepositoryAdapter implements Courses {

    private final CourseJpaRepository jpaRepository;
    private final DegreeJpaRepository degreeJpaRepository;
    private final InstitutionJpaRepository institutionJpaRepository;

    @Override
    public Course save(Course course) {
        CourseEntity entity = jpaRepository.findById(course.id())
                .orElse(CourseEntity.fromDomain(course));

        entity.setShortName(course.shortName());
        entity.setFullName(course.fullName());

        // Resolve degree relationships
        if (course.degreeIds() != null && !course.degreeIds().isEmpty()) {
            List<DegreeEntity> degrees = degreeJpaRepository.findAllById(course.degreeIds());
            entity.setDegrees(new HashSet<>(degrees));
        } else {
            entity.setDegrees(new HashSet<>());
        }

        // Resolve institution relationships
        if (course.institutionIds() != null && !course.institutionIds().isEmpty()) {
            List<InstitutionEntity> institutions = institutionJpaRepository.findAllById(course.institutionIds());
            entity.setInstitutions(new HashSet<>(institutions));
        } else {
            entity.setInstitutions(new HashSet<>());
        }

        CourseEntity saved = jpaRepository.save(entity);
        return saved.toDomain();
    }

    @Override
    public void delete(UUID id) {
        jpaRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Course> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(CourseEntity::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Course> findByShortNameAndFullName(String shortName, String fullName) {
        return jpaRepository.findByShortNameAndFullName(shortName, fullName)
                .map(CourseEntity::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Course> findAll() {
        return jpaRepository.findAll().stream()
                .map(CourseEntity::toDomain)
                .collect(Collectors.toList());
    }
}
