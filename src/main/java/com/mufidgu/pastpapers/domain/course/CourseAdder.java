package com.mufidgu.pastpapers.domain.course;

import com.mufidgu.pastpapers.domain.common.exception.ConflictException;
import com.mufidgu.pastpapers.domain.common.exception.NotFoundException;
import com.mufidgu.pastpapers.domain.course.api.AddCourse;
import com.mufidgu.pastpapers.domain.course.spi.Courses;
import com.mufidgu.pastpapers.domain.degree.spi.Degrees;
import ddd.DomainService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@DomainService
@RequiredArgsConstructor
public class CourseAdder implements AddCourse {

    private final Courses courses;
    private final Degrees degrees;

    public Course add(String shortName, String fullName, List<UUID> degreeIds) {
        courses.findByShortNameAndFullName(shortName, fullName)
                .ifPresent(course -> {
                    throw new ConflictException("Course with same short name and full name already exists");
                });
        degreeIds.forEach(id -> degrees.findById(id)
                .orElseThrow(() -> new NotFoundException("Degree with ID " + id + " does not exist")));

        Course course = new Course(shortName, fullName, degreeIds);
        return courses.save(course);
    }
}
