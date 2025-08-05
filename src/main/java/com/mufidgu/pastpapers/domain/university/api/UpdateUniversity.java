package com.mufidgu.pastpapers.domain.university.api;

import com.mufidgu.pastpapers.domain.university.University;

public interface UpdateUniversity {
    University updateUniversity(String id, String shortName, String fullName);
}
