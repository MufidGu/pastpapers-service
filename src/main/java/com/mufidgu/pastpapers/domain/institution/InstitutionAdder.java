package com.mufidgu.pastpapers.domain.institution;

import com.mufidgu.pastpapers.domain.common.exception.ConflictException;
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
            throw new ConflictException("Institution with same short name and full name already exists");
        });
        return institutions.save(
                new Institution(shortName, fullName)
        );
    }

}
