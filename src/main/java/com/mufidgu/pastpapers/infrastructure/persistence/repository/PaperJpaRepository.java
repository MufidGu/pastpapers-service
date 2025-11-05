package com.mufidgu.pastpapers.infrastructure.persistence.repository;

import com.mufidgu.pastpapers.infrastructure.persistence.entity.PaperEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA repository for PaperEntity.
 */
@Repository
public interface PaperJpaRepository extends JpaRepository<PaperEntity, UUID> {

    @Query("SELECT p FROM PaperEntity p WHERE p.id = :id AND p.user.googleId = :userId")
    Optional<PaperEntity> findByIdAndUserId(@Param("id") UUID id, @Param("userId") String userId);
}
