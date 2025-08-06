package com.mufidgu.pastpapers.infrastructure.controller.degree;

import java.util.List;
import java.util.UUID;

public record DegreeResource(UUID id, String shortName, String fullName, List<UUID> universities) {
}
