package com.mufidgu.pastpapers.domain.course.api;

import com.mufidgu.pastpapers.domain.course.Course;

import java.util.List;
import java.util.UUID;

public interface AddCourse {
    Course addCourse(String shortName, String fullName, List<UUID> degreeIds, List<UUID> universityIds);
}
