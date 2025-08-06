package com.mufidgu.pastpapers.domain.course;

import com.mufidgu.pastpapers.domain.course.api.DeleteCourse;
import com.mufidgu.pastpapers.domain.course.spi.Courses;
import com.mufidgu.pastpapers.domain.university.spi.Universities;
import ddd.DomainService;

import java.util.UUID;

@DomainService
public class CourseDeleter implements DeleteCourse {

    private final Courses courses;

    public CourseDeleter(Courses courses) {
        this.courses = courses;
    }

    public void delete(UUID courseId) {
        courses.findById(courseId)
            .orElseThrow(() -> new IllegalArgumentException("Course with ID " + courseId + " does not exist"));
        courses.delete(courseId);
    }
}
