package com.mufidgu.pastpapers.infrastructure.controller.instructor;

import com.mufidgu.pastpapers.domain.instructor.Instructor;
import com.mufidgu.pastpapers.domain.instructor.api.AddInstructor;
import com.mufidgu.pastpapers.domain.instructor.api.DeleteInstructor;
import com.mufidgu.pastpapers.domain.instructor.api.ListInstructor;
import com.mufidgu.pastpapers.domain.instructor.api.UpdateInstructor;
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
@RequestMapping("/instructor")
public class InstructorController {

    private final AddInstructor instructorAdder;
    private final UpdateInstructor instructorUpdater;
    private final DeleteInstructor instructorDeleter;
    private final ListInstructor instructorLister;

    @PostMapping("/add")
    public ResponseEntity<InstructorResource> add(@Valid @RequestBody InstructorRequest request) {
        Instructor instructor = instructorAdder.add(
                request.fullName,
                request.courseIds,
                request.institutionIds
        );
        return ResponseEntity.ok(
                InstructorResource.from(instructor)
        );
    }

    @PutMapping("/update")
    public ResponseEntity<InstructorResource> update(
            @NotBlank @RequestParam String instructorId,
            @Valid @RequestBody InstructorRequest request
    ) {
        UUID id = UUID.fromString(instructorId);
        Instructor instructor = instructorUpdater.update(
                id,
                request.fullName,
                request.courseIds,
                request.institutionIds
        );
        return ResponseEntity.ok(
                InstructorResource.from(instructor)
        );
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> delete(@NotBlank @RequestParam String instructorId) {
        UUID id = UUID.fromString(instructorId);
        instructorDeleter.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/list")
    public ResponseEntity<Iterable<InstructorResource>> list() {
        List<Instructor> instructors = instructorLister.listAll();
        return ResponseEntity.ok(instructors.stream()
                .map(InstructorResource::from)
                .toList());
    }
}
