package com.mufidgu.pastpapers.infrastructure.controller.course;

import com.mufidgu.pastpapers.domain.course.Course;
import com.mufidgu.pastpapers.domain.course.api.AddCourse;
import com.mufidgu.pastpapers.domain.course.api.DeleteCourse;
import com.mufidgu.pastpapers.domain.course.api.FetchCourse;
import com.mufidgu.pastpapers.domain.course.api.UpdateCourse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Validated
@RestController
@RequestMapping("/course")
public class CourseController {

    private final AddCourse courseAdder;
    private final UpdateCourse courseUpdater;
    private final DeleteCourse courseDeleter;
    private final FetchCourse courseFetcher;

    public CourseController(
            AddCourse courseAdder,
            UpdateCourse courseUpdater,
            DeleteCourse courseDeleter,
            FetchCourse courseFetcher) {
        this.courseAdder = courseAdder;
        this.courseUpdater = courseUpdater;
        this.courseDeleter = courseDeleter;
        this.courseFetcher = courseFetcher;
    }

    // TODO: Admin Only
    // Validation, Already Exists, Degree/University Does Not Exist
    @PostMapping("/add")
    public ResponseEntity<CourseResource> addCourse(@Valid @RequestBody CourseRequest request) {
        Course course = courseAdder.add(
                request.shortName,
                request.fullName,
                request.degreeIds,
                request.universityIds
        );
        return ResponseEntity.ok(new CourseResource(
                course.id(),
                course.shortName(),
                course.fullName(),
                course.degreeIds(),
                course.universityIds()
        ));
    }

    // TODO: Admin Only
    @PutMapping("/update")
    public ResponseEntity<CourseResource> updateCourse(
            @Valid @RequestParam String courseId,
            @Valid @RequestBody CourseRequest request
    ) {
        UUID id = UUID.fromString(courseId);
        Course course = courseUpdater.update(
                id,
                request.shortName,
                request.fullName,
                request.degreeIds,
                request.universityIds
        );
        return ResponseEntity.ok(new CourseResource(
                course.id(),
                course.shortName(),
                course.fullName(),
                course.degreeIds(),
                course.universityIds()
        ));
    }

    // TODO: Admin Only
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteCourse(@RequestParam String courseId) {
        UUID id = UUID.fromString(courseId);
        courseDeleter.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // TODO: Registered Users Only
    @GetMapping("/all")
    public ResponseEntity<Iterable<CourseResource>> getAllCourses() {
        var courses = courseFetcher.fetchAll();
        return ResponseEntity.ok(courses.stream()
                .map(c -> new CourseResource(
                        c.id(),
                        c.shortName(),
                        c.fullName(),
                        c.degreeIds(),
                        c.universityIds()))
                .toList());
    }

}
