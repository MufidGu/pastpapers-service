package com.mufidgu.pastpapers.infrastructure.repository;

import com.mufidgu.pastpapers.infrastructure.entities.DegreeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DegreeRepository extends JpaRepository<DegreeEntity, UUID> {
}
