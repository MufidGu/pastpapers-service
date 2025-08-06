package com.mufidgu.pastpapers.domain.degree.api;

import com.mufidgu.pastpapers.domain.degree.Degree;

import java.util.List;
import java.util.UUID;

public interface AddDegree {
    Degree addDegree(String shortName, String fullName, List<UUID> universities);
}
