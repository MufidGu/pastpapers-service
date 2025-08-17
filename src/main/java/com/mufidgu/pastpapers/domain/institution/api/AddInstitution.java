package com.mufidgu.pastpapers.domain.institution.api;

import com.mufidgu.pastpapers.domain.institution.Institution;

public interface AddInstitution {
    Institution add(String shortName, String fullName);
}
