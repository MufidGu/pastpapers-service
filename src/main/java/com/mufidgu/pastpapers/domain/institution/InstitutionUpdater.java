package com.mufidgu.pastpapers.domain.institution;

import com.mufidgu.pastpapers.domain.institution.api.UpdateInstitution;
import com.mufidgu.pastpapers.domain.institution.spi.Institutions;
import ddd.DomainService;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@DomainService
@RequiredArgsConstructor
public class InstitutionUpdater implements UpdateInstitution {

    private final Institutions institutions;

    public Institution update(UUID id, String shortName, String fullName) {
        // TODO: better exception handling
        institutions.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Institution does not exist.")
        );
        institutions.findByShortNameAndFullName(shortName, fullName).ifPresent(u -> {
            if (!u.id().equals(id)) {
                throw new IllegalArgumentException("Institution with the same short name and full name already exists.");
            }
        });

        // TODO: Revisit this when adding database to project
        Institution institution = new Institution(id, shortName, fullName);
        institutions.save(institution);

        return institution;
    }
}
