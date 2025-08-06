package com.mufidgu.pastpapers.infrastructure.controller.course;

import java.util.List;
import java.util.UUID;

public record CourseResource(
        UUID id,
        String shortName,
        String fullName,
        List<UUID> degreeIds,
        List<UUID> universityIds
) {
}