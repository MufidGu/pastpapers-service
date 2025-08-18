package com.mufidgu.pastpapers.domain.institution;

import com.mufidgu.pastpapers.domain.institution.api.DeleteInstitution;
import com.mufidgu.pastpapers.domain.institution.spi.Institutions;
import com.mufidgu.pastpapers.domain.common.exception.NotFoundException;
import ddd.DomainService;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@DomainService
@RequiredArgsConstructor
public class InstitutionDeleter implements DeleteInstitution {

    private final Institutions institutions;

    public void delete(UUID id) {
        institutions.findById(id).orElseThrow(
                () -> new NotFoundException("Institution does not exist.") // TODO: handle this properly
        );
        institutions.delete(id);
    }
}
