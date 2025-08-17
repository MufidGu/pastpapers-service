package com.mufidgu.pastpapers.domain.institution;

import java.util.UUID;

public record Institution(
        UUID id,
        String shortName,
        String fullName
) {
    public Institution(String shortName, String fullName) {
        this(UUID.randomUUID(), shortName, fullName);
    }
}
