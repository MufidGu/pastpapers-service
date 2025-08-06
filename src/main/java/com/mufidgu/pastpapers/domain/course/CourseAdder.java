package com.mufidgu.pastpapers.domain.course;

import com.mufidgu.pastpapers.domain.course.api.AddCourse;
import com.mufidgu.pastpapers.domain.course.spi.Courses;
import com.mufidgu.pastpapers.domain.degree.spi.Degrees;
import com.mufidgu.pastpapers.domain.university.spi.Universities;
import ddd.DomainService;

import java.util.List;
import java.util.UUID;

@DomainService
public class CourseAdder implements AddCourse {

    private final Courses courses;
    private final Degrees degrees;
    private final Universities universities;

    public CourseAdder(Courses courses, Degrees degrees, Universities universities) {
        this.courses = courses;
        this.degrees = degrees;
        this.universities = universities;
    }

    public Course addCourse(String shortName, String fullName, List<UUID> degreeIds, List<UUID> universityIds) {
        // TODO: better error handling
        courses.findByShortNameAndFullName(shortName, fullName)
                .ifPresent(course -> {
                    throw new IllegalArgumentException("Course with the same short name and full name already exists");
                });
        degreeIds.forEach(id -> degrees.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Degree with ID " + id + " does not exist")));
        universityIds.forEach(id -> universities.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("University with ID " + id + " does not exist")));

        Course course = new Course(shortName, fullName, degreeIds, universityIds);
        return courses.save(course);
    }
}
