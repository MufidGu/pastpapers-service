package com.mufidgu.pastpapers.domain.university.spi;

import com.mufidgu.pastpapers.domain.university.University;

import java.util.List;

public interface Universities {
    University save(University university);
    University findByShortNameAndFullName(String shortName, String fullName);
    List<University> getAll();
    University findById(String id);
}
