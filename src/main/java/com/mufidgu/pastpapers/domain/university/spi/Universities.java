package com.mufidgu.pastpapers.domain.university.spi;

import com.mufidgu.pastpapers.domain.university.University;

import java.util.List;

public interface Universities {
    void clear();
    University save(University university);
    University getByShortNameAndFullName(String shortName, String fullName);
    List<University> getAll();
}
