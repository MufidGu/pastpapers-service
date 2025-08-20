package com.mufidgu.pastpapers.infrastructure.controller.degree;

import com.mufidgu.pastpapers.domain.degree.Degree;

import java.util.List;
import java.util.UUID;

public record DegreeResource(
        UUID id,
        String shortName,
        String fullName,
        List<UUID> institutions
) {
    public static DegreeResource from(Degree degree) {
        return new DegreeResource(
                degree.id(),
                degree.shortName(),
                degree.fullName(),
                degree.institutions()
        );
    }
}
