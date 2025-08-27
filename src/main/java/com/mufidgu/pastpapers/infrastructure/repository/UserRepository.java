package com.mufidgu.pastpapers.infrastructure.repository;

import com.mufidgu.pastpapers.infrastructure.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
    Optional<UserEntity> findByGoogleId(String googleId);
}
