package com.mufidgu.pastpapers.infrastructure.repository;

import com.mufidgu.pastpapers.infrastructure.entities.PaperEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PaperRepository extends JpaRepository<PaperEntity, UUID> {
}
