package com.mufidgu.pastpapers.domain.university.spi;

import com.mufidgu.pastpapers.domain.university.University;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface Universities {
    University save(University university);
    Optional<University> findByShortNameAndFullName(String shortName, String fullName);
    List<University> findAll();
    Optional<University> findById(UUID id);
    void delete(UUID id);
}
