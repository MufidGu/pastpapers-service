package com.mufidgu.pastpapers.domain.course;

import com.mufidgu.pastpapers.domain.course.api.FetchCourse;
import com.mufidgu.pastpapers.domain.course.spi.Courses;
import ddd.DomainService;

import java.util.List;

@DomainService
public class CourseFetcher implements FetchCourse {

    private final Courses courses;

    public CourseFetcher(Courses courses) {
        this.courses = courses;
    }

    public List<Course> fetchAll() {
        return courses.findAll();
    }
}
