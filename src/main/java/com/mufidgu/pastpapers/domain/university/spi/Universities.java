package com.mufidgu.pastpapers.domain.university.spi;

import com.mufidgu.pastpapers.domain.university.University;

import java.util.List;
import java.util.UUID;

public interface Universities {
    University save(University university);
    University findByShortNameAndFullName(String shortName, String fullName);
    List<University> getAll();
    University findById(UUID id);
    void delete(UUID id);
}
