package com.mufidgu.pastpapers.domain.institution;

import com.mufidgu.pastpapers.domain.institution.api.AddInstitution;
import com.mufidgu.pastpapers.domain.institution.spi.Institutions;
import ddd.DomainService;
import lombok.RequiredArgsConstructor;

@DomainService
@RequiredArgsConstructor
public class InstitutionAdder implements AddInstitution {

    private final Institutions institutions;

    public Institution add(String shortName, String fullName) {
        institutions.findByShortNameAndFullName(shortName, fullName).ifPresent(u -> {
            throw new IllegalArgumentException("Institution already exists."); // TODO: handle this properly
        });
        Institution institution = new Institution(shortName, fullName);
        return institutions.save(institution);
    }

}
