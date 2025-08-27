package com.mufidgu.pastpapers.infrastructure.controller.instructor;

import com.mufidgu.pastpapers.domain.instructor.Instructor;

import java.util.List;
import java.util.UUID;

public record InstructorResource(
        UUID id,
        String fullName,
        List<UUID> courseIds
) {

    public static InstructorResource from(Instructor instructor) {
        return new InstructorResource(
                instructor.id(),
                instructor.fullName(),
                instructor.courseIds()
        );
    }
}
