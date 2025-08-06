package com.mufidgu.pastpapers.domain.course;

import java.util.List;
import java.util.UUID;

public record Course(
        UUID id,
        String shortName,
        String fullName,
        List<UUID> degreeIds,
        List<UUID> universityIds
) {
    public Course(String shortName, String fullName, List<UUID> degreeIds, List<UUID> universityIds) {
        this(UUID.randomUUID(), shortName, fullName, degreeIds, universityIds);
    }
}
