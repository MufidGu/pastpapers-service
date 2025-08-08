package com.mufidgu.pastpapers.domain.instructor.api;

import com.mufidgu.pastpapers.domain.instructor.Instructor;

import java.util.List;
import java.util.UUID;

public interface AddInstructor {
    Instructor add(String fullName, List<UUID> courseIds, List<UUID> universityIds);
}
