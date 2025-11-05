package com.mufidgu.pastpapers.infrastructure.persistence.adapter;

import com.mufidgu.pastpapers.domain.institution.Institution;
import com.mufidgu.pastpapers.domain.institution.spi.Institutions;
import com.mufidgu.pastpapers.infrastructure.persistence.entity.InstitutionEntity;
import com.mufidgu.pastpapers.infrastructure.persistence.repository.InstitutionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * JPA adapter implementation for the Institutions SPI.
 * Converts between domain records and JPA entities.
 * Disabled in test profile to allow stub implementations.
 */
@Repository
@Primary
@Profile("!test")
@RequiredArgsConstructor
@Transactional
public class InstitutionRepositoryAdapter implements Institutions {

    private final InstitutionJpaRepository jpaRepository;

    @Override
    public Institution save(Institution institution) {
        InstitutionEntity entity = InstitutionEntity.fromDomain(institution);
        InstitutionEntity saved = jpaRepository.save(entity);
        return saved.toDomain();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Institution> findByShortNameAndFullName(String shortName, String fullName) {
        return jpaRepository.findByShortNameAndFullName(shortName, fullName)
                .map(InstitutionEntity::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Institution> findAll() {
        return jpaRepository.findAll().stream()
                .map(InstitutionEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Institution> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(InstitutionEntity::toDomain);
    }

    @Override
    public void delete(UUID id) {
        jpaRepository.deleteById(id);
    }
}
