package com.mufidgu.pastpapers.domain.institution.spi;

import com.mufidgu.pastpapers.domain.institution.Institution;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface Institutions {
    Institution save(Institution institution);

    Optional<Institution> findByShortNameAndFullName(String shortName, String fullName);

    List<Institution> findAll();

    Optional<Institution> findById(UUID id);

    void delete(UUID id);
}
