package com.mufidgu.pastpapers.domain.instructor;

import com.mufidgu.pastpapers.domain.instructor.api.AddInstructor;
import com.mufidgu.pastpapers.domain.instructor.spi.Instructors;
import com.mufidgu.pastpapers.domain.course.spi.Courses;
import com.mufidgu.pastpapers.domain.institution.spi.Institutions;
import ddd.DomainService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@DomainService
@RequiredArgsConstructor
public class InstructorAdder implements AddInstructor {

    private final Instructors instructors;
    private final Courses courses;
    private final Institutions institutions;

    @Override
    public Instructor add(String fullName, List<UUID> courseIds, List<UUID> institutionIds) {
        instructors.findByFullName(fullName)
                .ifPresent(instructor -> {
                    throw new IllegalArgumentException("Instructor with the same full name already exists");
                });

        courseIds.forEach(id -> courses.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Course with ID " + id + " does not exist")));

        institutionIds.forEach(id -> institutions.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Institution with ID " + id + " does not exist")));

        Instructor instructor = new Instructor(fullName, courseIds, institutionIds);
        return instructors.save(instructor);
    }
}
