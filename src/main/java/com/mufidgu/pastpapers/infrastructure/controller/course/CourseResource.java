package com.mufidgu.pastpapers.infrastructure.controller.course;

import com.mufidgu.pastpapers.domain.course.Course;

import java.util.List;
import java.util.UUID;

public record CourseResource(
        UUID id,
        String shortName,
        String fullName,
        List<UUID> degreeIds
) {
    public static CourseResource from(Course course) {
        return new CourseResource(
                course.id(),
                course.shortName(),
                course.fullName(),
                course.degreeIds()
        );
    }
}