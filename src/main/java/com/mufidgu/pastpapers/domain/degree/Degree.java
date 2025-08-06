package com.mufidgu.pastpapers.domain.degree;

import java.util.List;
import java.util.UUID;

public record Degree(UUID id, String shortName, String fullName, List<UUID> universities) {
    public Degree(String shortName, String fullName, List<UUID> universities) {
        this(UUID.randomUUID(), shortName, fullName, universities);
    }
}
