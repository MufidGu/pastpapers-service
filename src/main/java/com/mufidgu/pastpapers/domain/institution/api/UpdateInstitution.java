package com.mufidgu.pastpapers.domain.institution.api;

import com.mufidgu.pastpapers.domain.institution.Institution;

import java.util.UUID;

public interface UpdateInstitution {
    Institution update(UUID id, String shortName, String fullName);
}
