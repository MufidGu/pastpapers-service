package com.mufidgu.pastpapers.domain.course;

import com.mufidgu.pastpapers.domain.course.api.UpdateCourse;
import com.mufidgu.pastpapers.domain.course.spi.Courses;
import com.mufidgu.pastpapers.domain.university.spi.Universities;
import ddd.DomainService;

import java.util.List;
import java.util.UUID;

@DomainService
public class CourseUpdater implements UpdateCourse {

    private final Courses courses;
    private final Universities universities;

    public CourseUpdater(Courses courses, Universities universities) {
        this.courses = courses;
        this.universities = universities;
    }

    public Course updateCourse(UUID id, String shortName, String fullName, List<UUID> degreeIds, List<UUID> universityIds) {
        // TODO: better error handling
        Course existingCourse = courses.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Course does not exist"));
        courses.findByShortNameAndFullName(shortName, fullName)
                .ifPresent(course -> {
                    throw new IllegalArgumentException("Course with the same short name and full name already exists");
                });
        degreeIds.forEach(degreeId -> {
            if (universities.findById(degreeId).isEmpty()) {
                throw new IllegalArgumentException("Degree with ID " + degreeId + " does not exist");
            }
        });
        universityIds.forEach(universityId -> {
            if (universities.findById(universityId).isEmpty()) {
                throw new IllegalArgumentException("University with ID " + universityId + " does not exist");
            }
        });

        // TODO: Revisit this when adding database to project
        return courses.save(
                new Course(
                        existingCourse.id(),
                        shortName,
                        fullName,
                        degreeIds,
                        universityIds
                )
        );
    }
}
