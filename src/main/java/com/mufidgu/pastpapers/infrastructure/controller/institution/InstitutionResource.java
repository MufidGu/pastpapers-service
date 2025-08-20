package com.mufidgu.pastpapers.infrastructure.controller.institution;

import com.mufidgu.pastpapers.domain.institution.Institution;

import java.util.UUID;

public record InstitutionResource(
        UUID id,
        String shortName,
        String fullName
) {

    public static InstitutionResource from(Institution institution) {
        return new InstitutionResource(
                institution.id(),
                institution.shortName(),
                institution.fullName()
        );
    }
}
