package com.mufidgu.pastpapers.infrastructure.controller.course;

import com.mufidgu.pastpapers.domain.course.Course;
import com.mufidgu.pastpapers.domain.course.api.AddCourse;
import com.mufidgu.pastpapers.domain.course.api.DeleteCourse;
import com.mufidgu.pastpapers.domain.course.api.ListCourse;
import com.mufidgu.pastpapers.domain.course.api.UpdateCourse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/course")
public class CourseController {

    private final AddCourse courseAdder;
    private final UpdateCourse courseUpdater;
    private final DeleteCourse courseDeleter;
    private final ListCourse courseLister;

    // Validation, Already Exists, Degree/Institution Does Not Exist
    @PostMapping("/add")
    public ResponseEntity<CourseResource> add(@Valid @RequestBody CourseRequest request) {
        Course course = courseAdder.add(
                request.shortName,
                request.fullName,
                request.degreeIds,
                request.institutionIds
        );
        return ResponseEntity.ok(
                CourseResource.from(course)
        );
    }

    @PutMapping("/update")
    public ResponseEntity<CourseResource> update(
            @NotBlank @RequestParam String courseId,
            @Valid @RequestBody CourseRequest request
    ) {
        UUID id = UUID.fromString(courseId);
        Course course = courseUpdater.update(
                id,
                request.shortName,
                request.fullName,
                request.degreeIds,
                request.institutionIds
        );
        return ResponseEntity.ok(
                CourseResource.from(course)
        );
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> delete(@NotBlank @RequestParam String courseId) {
        UUID id = UUID.fromString(courseId);
        courseDeleter.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/list")
    public ResponseEntity<List<CourseResource>> list() {
        List<Course> courses = courseLister.listAll();
        return ResponseEntity.ok(courses.stream()
                .map(CourseResource::from)
                .toList());
    }

}
