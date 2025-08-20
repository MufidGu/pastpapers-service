package com.mufidgu.pastpapers.domain.institution;

import com.mufidgu.pastpapers.domain.common.exception.ConflictException;
import com.mufidgu.pastpapers.domain.common.exception.NotFoundException;
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
        institutions.findById(id).orElseThrow(
                () -> new NotFoundException("Institution with same short name and full name already exists")
        );
        institutions.findByShortNameAndFullName(shortName, fullName).ifPresent(i -> {
            if (!i.id().equals(id)) {
                throw new ConflictException("Institution with same short name and full name already exists");
            }
        });

        return institutions.save(
                new Institution(id, shortName, fullName)
        );
    }
}
