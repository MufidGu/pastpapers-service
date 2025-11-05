package com.mufidgu.pastpapers.infrastructure.persistence.adapter;

import com.mufidgu.pastpapers.domain.degree.Degree;
import com.mufidgu.pastpapers.domain.degree.spi.Degrees;
import com.mufidgu.pastpapers.infrastructure.persistence.entity.DegreeEntity;
import com.mufidgu.pastpapers.infrastructure.persistence.entity.InstitutionEntity;
import com.mufidgu.pastpapers.infrastructure.persistence.repository.DegreeJpaRepository;
import com.mufidgu.pastpapers.infrastructure.persistence.repository.InstitutionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * JPA adapter implementation for the Degrees SPI.
 * Handles many-to-many relationships with institutions.
 */
@Repository
@Primary
@RequiredArgsConstructor
@Transactional
public class DegreeRepositoryAdapter implements Degrees {

    private final DegreeJpaRepository jpaRepository;
    private final InstitutionJpaRepository institutionJpaRepository;

    @Override
    public Degree save(Degree degree) {
        DegreeEntity entity = jpaRepository.findById(degree.id())
                .orElse(DegreeEntity.fromDomain(degree));

        entity.setShortName(degree.shortName());
        entity.setFullName(degree.fullName());

        // Resolve institution relationships
        if (degree.institutions() != null && !degree.institutions().isEmpty()) {
            List<InstitutionEntity> institutions = institutionJpaRepository.findAllById(degree.institutions());
            entity.setInstitutions(new HashSet<>(institutions));
        } else {
            entity.setInstitutions(new HashSet<>());
        }

        DegreeEntity saved = jpaRepository.save(entity);
        return saved.toDomain();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Degree> findByShortNameAndFullName(String shortName, String fullName) {
        return jpaRepository.findByShortNameAndFullName(shortName, fullName)
                .map(DegreeEntity::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Degree> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(DegreeEntity::toDomain);
    }

    @Override
    public void delete(UUID id) {
        jpaRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Degree> findAll() {
        return jpaRepository.findAll().stream()
                .map(DegreeEntity::toDomain)
                .collect(Collectors.toList());
    }
}
