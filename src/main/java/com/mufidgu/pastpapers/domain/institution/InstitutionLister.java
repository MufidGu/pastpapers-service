package com.mufidgu.pastpapers.domain.institution;

import com.mufidgu.pastpapers.domain.institution.api.ListInstitution;
import com.mufidgu.pastpapers.domain.institution.spi.Institutions;
import ddd.DomainService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@DomainService
@RequiredArgsConstructor
public class InstitutionLister implements ListInstitution {

    private final Institutions institutions;

    public List<Institution> listAll() {
        return institutions.findAll();
    }
}
