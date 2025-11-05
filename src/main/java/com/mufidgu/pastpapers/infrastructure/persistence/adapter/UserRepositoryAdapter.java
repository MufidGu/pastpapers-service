package com.mufidgu.pastpapers.infrastructure.persistence.adapter;

import com.mufidgu.pastpapers.domain.user.User;
import com.mufidgu.pastpapers.domain.user.spi.Users;
import com.mufidgu.pastpapers.infrastructure.persistence.entity.DegreeEntity;
import com.mufidgu.pastpapers.infrastructure.persistence.entity.InstitutionEntity;
import com.mufidgu.pastpapers.infrastructure.persistence.entity.UserEntity;
import com.mufidgu.pastpapers.infrastructure.persistence.repository.DegreeJpaRepository;
import com.mufidgu.pastpapers.infrastructure.persistence.repository.InstitutionJpaRepository;
import com.mufidgu.pastpapers.infrastructure.persistence.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * JPA adapter implementation for the Users SPI.
 * Handles many-to-one relationships with institution and degree.
 * Disabled in test profile to allow stub implementations.
 */
@Repository
@Primary
@Profile("!test")
@RequiredArgsConstructor
@Transactional
public class UserRepositoryAdapter implements Users {

    private final UserJpaRepository jpaRepository;
    private final InstitutionJpaRepository institutionJpaRepository;
    private final DegreeJpaRepository degreeJpaRepository;

    @Override
    public User save(User user) {
        UserEntity entity = jpaRepository.findById(user.googleId())
                .orElse(UserEntity.fromDomain(user));

        // Update basic fields
        entity.setSessionStartDate(user.sessionStartDate());
        entity.setSessionStartSeason(user.sessionStartSeason());
        entity.setSemester(user.semester());
        entity.setSection(user.section());
        entity.setShift(user.shift());

        // Resolve institution relationship
        if (user.institutionId() != null) {
            InstitutionEntity institution = institutionJpaRepository.findById(user.institutionId())
                    .orElse(null);
            entity.setInstitution(institution);
        } else {
            entity.setInstitution(null);
        }

        // Resolve degree relationship
        if (user.degreeId() != null) {
            DegreeEntity degree = degreeJpaRepository.findById(user.degreeId())
                    .orElse(null);
            entity.setDegree(degree);
        } else {
            entity.setDegree(null);
        }

        UserEntity saved = jpaRepository.save(entity);
        return saved.toDomain();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByGoogleId(String googleId) {
        return jpaRepository.findById(googleId)
                .map(UserEntity::toDomain);
    }
}
