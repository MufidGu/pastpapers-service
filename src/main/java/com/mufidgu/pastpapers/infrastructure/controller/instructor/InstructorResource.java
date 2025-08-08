package com.mufidgu.pastpapers.infrastructure.controller.instructor;

import java.util.List;
import java.util.UUID;

public record InstructorResource(
        UUID id,
        String fullName,
        List<UUID> courseIds,
        List<UUID> universityIds
) {
}
