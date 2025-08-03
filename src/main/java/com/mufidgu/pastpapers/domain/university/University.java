package com.mufidgu.pastpapers.domain.university;

import java.util.UUID;

public record University(
        UUID id,
        String shortName,
        String fullName
) {
    public University(String shortName, String fullName) {
        this(UUID.randomUUID(), shortName, fullName);
    }
}
