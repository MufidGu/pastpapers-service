package com.mufidgu.pastpapers.domain.course;

import java.util.List;
import java.util.UUID;

public record Course(
        UUID id,
        String shortName,
        String fullName,
        List<UUID> degreeIds
) {
    public Course(String shortName, String fullName, List<UUID> degreeIds) {
        this(UUID.randomUUID(), shortName, fullName, degreeIds);
    }
}
