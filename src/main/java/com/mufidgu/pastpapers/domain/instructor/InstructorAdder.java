package com.mufidgu.pastpapers.domain.instructor;

import com.mufidgu.pastpapers.domain.instructor.api.AddInstructor;
import com.mufidgu.pastpapers.domain.instructor.spi.Instructors;
import com.mufidgu.pastpapers.domain.course.spi.Courses;
import com.mufidgu.pastpapers.domain.university.spi.Universities;
import ddd.DomainService;

import java.util.List;
import java.util.UUID;

@DomainService
public class InstructorAdder implements AddInstructor {

    private final Instructors instructors;
    private final Courses courses;
    private final Universities universities;

    public InstructorAdder(Instructors instructors, Courses courses, Universities universities) {
        this.instructors = instructors;
        this.courses = courses;
        this.universities = universities;
    }

    @Override
    public Instructor add(String fullName, List<UUID> courseIds, List<UUID> universityIds) {
        instructors.findByFullName(fullName)
                .ifPresent(instructor -> {
                    throw new IllegalArgumentException("Instructor with the same full name already exists");
                });

        courseIds.forEach(id -> courses.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Course with ID " + id + " does not exist")));

        universityIds.forEach(id -> universities.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("University with ID " + id + " does not exist")));

        Instructor instructor = new Instructor(fullName, courseIds, universityIds);
        return instructors.save(instructor);
    }
}
