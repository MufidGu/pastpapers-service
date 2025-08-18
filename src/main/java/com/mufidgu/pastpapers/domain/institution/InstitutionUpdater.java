package com.mufidgu.pastpapers.domain.institution;

import com.mufidgu.pastpapers.domain.institution.api.UpdateInstitution;
import com.mufidgu.pastpapers.domain.institution.spi.Institutions;
import com.mufidgu.pastpapers.domain.common.exception.ConflictException;
import com.mufidgu.pastpapers.domain.common.exception.NotFoundException;
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
                () -> new NotFoundException("Institution does not exist.")
        );
        institutions.findByShortNameAndFullName(shortName, fullName).ifPresent(u -> {
            if (!u.id().equals(id)) {
                throw new ConflictException("Institution with the same short name and full name already exists.");
            }
        });

        // TODO: Revisit this when adding database to project
        Institution institution = new Institution(id, shortName, fullName);
        institutions.save(institution);

        return institution;
    }
}
