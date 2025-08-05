package com.mufidgu.pastpapers.domain.university.api;

import com.mufidgu.pastpapers.domain.university.University;

import java.util.UUID;

public interface UpdateUniversity {
    University update(UUID id, String shortName, String fullName);
}
