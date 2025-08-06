package com.mufidgu.pastpapers.domain.course.spi.stub;

import com.mufidgu.pastpapers.domain.course.Course;
import com.mufidgu.pastpapers.domain.course.spi.Courses;
import ddd.Stub;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Stub
public class InMemoryCourses implements Courses {

    private final HashMap<UUID, Course> courses = new HashMap<>();

    public Course save(Course course) {
        courses.put(course.id(), course);
        return course;
    }

    public void delete(UUID id) {
        courses.remove(id);
    }

    public Optional<Course> findById(UUID id) {
        return Optional.ofNullable(courses.get(id));
    }

    public Optional<Course> findByShortNameAndFullName(String shortName, String fullName) {
        return courses.values().stream()
                .filter(course -> course.shortName().equals(shortName) && course.fullName().equals(fullName))
                .findFirst();
    }

    public List<Course> findAll() {
        return List.copyOf(courses.values());
    }
}
