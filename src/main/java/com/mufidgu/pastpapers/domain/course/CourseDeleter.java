package com.mufidgu.pastpapers.domain.course;

import com.mufidgu.pastpapers.domain.course.api.DeleteCourse;
import com.mufidgu.pastpapers.domain.course.spi.Courses;
import com.mufidgu.pastpapers.domain.common.exception.NotFoundException;
import ddd.DomainService;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@DomainService
@RequiredArgsConstructor
public class CourseDeleter implements DeleteCourse {

    private final Courses courses;

    public void delete(UUID courseId) {
        courses.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course does not exist"));
        courses.delete(courseId);
    }
}
