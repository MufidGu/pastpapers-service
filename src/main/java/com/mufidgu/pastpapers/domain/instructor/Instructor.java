package com.mufidgu.pastpapers.domain.instructor;

import java.util.List;
import java.util.UUID;

public record Instructor(
        UUID id,
        String fullName,
        List<UUID> courseIds,
        List<UUID> institutionIds
) {
    public Instructor(String fullName, List<UUID> courseIds, List<UUID> institutionIds) {
        this(UUID.randomUUID(), fullName, courseIds, institutionIds);
    }
}
