package com.mufidgu.pastpapers.domain.course;

import com.mufidgu.pastpapers.domain.course.api.ListCourse;
import com.mufidgu.pastpapers.domain.course.spi.Courses;
import ddd.DomainService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@DomainService
@RequiredArgsConstructor
public class CourseLister implements ListCourse {

    private final Courses courses;

    public List<Course> listAll() {
        return courses.findAll();
    }
}
