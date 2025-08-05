package com.mufidgu.pastpapers.domain.university.api;

import com.mufidgu.pastpapers.domain.university.University;

public interface AddUniversity {
    University add(String shortName, String fullName);
}
