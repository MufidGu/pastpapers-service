package com.mufidgu.pastpapers.domain.institution;

import com.mufidgu.pastpapers.domain.institution.api.FetchInstitution;
import com.mufidgu.pastpapers.domain.institution.spi.Institutions;
import ddd.DomainService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@DomainService
@RequiredArgsConstructor
public class InstitutionFetcher implements FetchInstitution {

    private final Institutions institutions;

    public List<Institution> fetchAll() {
        return institutions.findAll();
    }
}
