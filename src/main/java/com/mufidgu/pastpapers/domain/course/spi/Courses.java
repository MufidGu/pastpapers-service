package com.mufidgu.pastpapers.domain.course.spi;

import com.mufidgu.pastpapers.domain.course.Course;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface Courses {
    Course save(Course course);

    void delete(UUID id);

    Optional<Course> findById(UUID id);

    Optional<Course> findByShortNameAndFullName(String shortName, String fullName);

    List<Course> findAll();
}
