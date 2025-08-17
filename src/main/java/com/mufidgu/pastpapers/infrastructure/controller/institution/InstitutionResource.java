package com.mufidgu.pastpapers.infrastructure.controller.institution;

import java.util.UUID;

public record InstitutionResource(
        UUID id,
        String shortName,
        String fullName
) {
}
