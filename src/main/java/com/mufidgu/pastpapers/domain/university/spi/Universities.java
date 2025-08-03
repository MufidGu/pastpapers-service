package com.mufidgu.pastpapers.domain.university.spi;

import com.mufidgu.pastpapers.domain.university.University;

public interface Universities {
    University save(University university);
    University getByShortNameAndFullName(String shortName, String fullName);
}
