package com.mufidgu.pastpapers.infrastructure.controller.university;

import java.util.UUID;

public record UniversityResource(
        UUID id,
        String shortName,
        String fullName
) {
}
