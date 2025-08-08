package com.mufidgu.pastpapers.domain.instructor;

import com.mufidgu.pastpapers.domain.instructor.api.UpdateInstructor;
import com.mufidgu.pastpapers.domain.instructor.spi.Instructors;
import com.mufidgu.pastpapers.domain.course.spi.Courses;
import com.mufidgu.pastpapers.domain.university.spi.Universities;
import ddd.DomainService;

import java.util.List;
import java.util.UUID;

@DomainService
public class InstructorUpdater implements UpdateInstructor {

    private final Instructors instructors;
    private final Courses courses;
    private final Universities universities;

    public InstructorUpdater(Instructors instructors, Courses courses, Universities universities) {
        this.instructors = instructors;
        this.courses = courses;
        this.universities = universities;
    }

    @Override
    public Instructor update(UUID id, String fullName, List<UUID> courseIds, List<UUID> universityIds) {
        Instructor existingInstructor = instructors.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Instructor does not exist"));

        instructors.findByFullName(fullName)
                .ifPresent(instructor -> {
                    if (!instructor.id().equals(existingInstructor.id())) {
                        throw new IllegalArgumentException("Instructor with the same full name already exists");
                    }
                });

        courseIds.forEach(courseId -> {
            if (courses.findById(courseId).isEmpty()) {
                throw new IllegalArgumentException("Course with ID " + courseId + " does not exist");
            }
        });

        universityIds.forEach(universityId -> {
            if (universities.findById(universityId).isEmpty()) {
                throw new IllegalArgumentException("University with ID " + universityId + " does not exist");
            }
        });

        return instructors.save(
                new Instructor(
                        existingInstructor.id(),
                        fullName,
                        courseIds,
                        universityIds
                )
        );
    }
}
