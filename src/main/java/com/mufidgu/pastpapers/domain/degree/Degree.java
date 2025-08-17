package com.mufidgu.pastpapers.domain.degree;

import java.util.List;
import java.util.UUID;

public record Degree(UUID id, String shortName, String fullName, List<UUID> institutions) {
    public Degree(String shortName, String fullName, List<UUID> institutions) {
        this(UUID.randomUUID(), shortName, fullName, institutions);
    }
}
