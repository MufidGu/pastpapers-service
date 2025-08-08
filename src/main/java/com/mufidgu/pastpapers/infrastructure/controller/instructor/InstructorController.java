package com.mufidgu.pastpapers.infrastructure.controller.instructor;

import com.mufidgu.pastpapers.domain.instructor.Instructor;
import com.mufidgu.pastpapers.domain.instructor.api.AddInstructor;
import com.mufidgu.pastpapers.domain.instructor.api.DeleteInstructor;
import com.mufidgu.pastpapers.domain.instructor.api.FetchInstructor;
import com.mufidgu.pastpapers.domain.instructor.api.UpdateInstructor;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Validated
@RestController
@RequestMapping("/instructor")
public class InstructorController {

    private final AddInstructor instructorAdder;
    private final UpdateInstructor instructorUpdater;
    private final DeleteInstructor instructorDeleter;
    private final FetchInstructor instructorFetcher;

    public InstructorController(
            AddInstructor instructorAdder,
            UpdateInstructor instructorUpdater,
            DeleteInstructor instructorDeleter,
            FetchInstructor instructorFetcher) {
        this.instructorAdder = instructorAdder;
        this.instructorUpdater = instructorUpdater;
        this.instructorDeleter = instructorDeleter;
        this.instructorFetcher = instructorFetcher;
    }

    // TODO: Admin Only
    @PostMapping("/add")
    public ResponseEntity<InstructorResource> addInstructor(@Valid @RequestBody InstructorRequest request) {
        Instructor instructor = instructorAdder.add(
                request.fullName,
                request.courseIds,
                request.universityIds
        );
        return ResponseEntity.ok(new InstructorResource(
                instructor.id(),
                instructor.fullName(),
                instructor.courseIds(),
                instructor.universityIds()
        ));
    }

    // TODO: Admin Only
    @PutMapping("/update")
    public ResponseEntity<InstructorResource> updateInstructor(
            @NotBlank @RequestParam String instructorId,
            @Valid @RequestBody InstructorRequest request
    ) {
        UUID id = UUID.fromString(instructorId);
        Instructor instructor = instructorUpdater.update(
                id,
                request.fullName,
                request.courseIds,
                request.universityIds
        );
        return ResponseEntity.ok(new InstructorResource(
                instructor.id(),
                instructor.fullName(),
                instructor.courseIds(),
                instructor.universityIds()
        ));
    }

    // TODO: Admin Only
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteInstructor(@NotBlank @RequestParam String instructorId) {
        UUID id = UUID.fromString(instructorId);
        instructorDeleter.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // TODO: Registered Users Only
    @GetMapping("/all")
    public ResponseEntity<Iterable<InstructorResource>> getAllInstructors() {
        var instructors = instructorFetcher.fetchAll();
        return ResponseEntity.ok(instructors.stream()
                .map(i -> new InstructorResource(
                        i.id(),
                        i.fullName(),
                        i.courseIds(),
                        i.universityIds()))
                .toList());
    }
}
