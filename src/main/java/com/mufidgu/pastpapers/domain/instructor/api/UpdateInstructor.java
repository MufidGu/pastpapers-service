package com.mufidgu.pastpapers.domain.instructor.api;

import com.mufidgu.pastpapers.domain.instructor.Instructor;

import java.util.List;
import java.util.UUID;

public interface UpdateInstructor {
    Instructor update(UUID id, String fullName, List<UUID> courseIds, List<UUID> universityIds);
}
