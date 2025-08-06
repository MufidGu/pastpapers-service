package com.mufidgu.pastpapers.domain.degree.spi;

import com.mufidgu.pastpapers.domain.degree.Degree;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface Degrees {
    Degree save(Degree degree);

    Optional<Degree> findByShortNameAndFullName(String shortName, String fullName);

    Optional<Degree> findById(UUID id);

    void delete(UUID id);

    List<Degree> findAll();
}
