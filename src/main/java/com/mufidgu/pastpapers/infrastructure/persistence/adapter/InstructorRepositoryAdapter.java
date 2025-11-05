package com.mufidgu.pastpapers.infrastructure.persistence.adapter;

import com.mufidgu.pastpapers.domain.instructor.Instructor;
import com.mufidgu.pastpapers.domain.instructor.spi.Instructors;
import com.mufidgu.pastpapers.infrastructure.persistence.entity.CourseEntity;
import com.mufidgu.pastpapers.infrastructure.persistence.entity.InstitutionEntity;
import com.mufidgu.pastpapers.infrastructure.persistence.entity.InstructorEntity;
import com.mufidgu.pastpapers.infrastructure.persistence.repository.CourseJpaRepository;
import com.mufidgu.pastpapers.infrastructure.persistence.repository.InstitutionJpaRepository;
import com.mufidgu.pastpapers.infrastructure.persistence.repository.InstructorJpaRepository;
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
 * JPA adapter implementation for the Instructors SPI.
 * Handles many-to-many relationships with courses and institutions.
 * Disabled in test profile to allow stub implementations.
 */
@Repository
@Primary
@Profile("!test")
@RequiredArgsConstructor
@Transactional
public class InstructorRepositoryAdapter implements Instructors {

    private final InstructorJpaRepository jpaRepository;
    private final CourseJpaRepository courseJpaRepository;
    private final InstitutionJpaRepository institutionJpaRepository;

    @Override
    public Instructor save(Instructor instructor) {
        InstructorEntity entity = jpaRepository.findById(instructor.id())
                .orElse(InstructorEntity.fromDomain(instructor));

        entity.setFullName(instructor.fullName());

        // Resolve course relationships
        if (instructor.courseIds() != null && !instructor.courseIds().isEmpty()) {
            List<CourseEntity> courses = courseJpaRepository.findAllById(instructor.courseIds());
            entity.setCourses(new HashSet<>(courses));
        } else {
            entity.setCourses(new HashSet<>());
        }

        // Resolve institution relationships
        if (instructor.institutionIds() != null && !instructor.institutionIds().isEmpty()) {
            List<InstitutionEntity> institutions = institutionJpaRepository.findAllById(instructor.institutionIds());
            entity.setInstitutions(new HashSet<>(institutions));
        } else {
            entity.setInstitutions(new HashSet<>());
        }

        InstructorEntity saved = jpaRepository.save(entity);
        return saved.toDomain();
    }

    @Override
    public void delete(UUID id) {
        jpaRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Instructor> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(InstructorEntity::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Instructor> findByFullName(String fullName) {
        return jpaRepository.findByFullName(fullName)
                .map(InstructorEntity::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Instructor> findAll() {
        return jpaRepository.findAll().stream()
                .map(InstructorEntity::toDomain)
                .collect(Collectors.toList());
    }
}
