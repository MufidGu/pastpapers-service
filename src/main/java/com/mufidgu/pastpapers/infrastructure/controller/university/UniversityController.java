package com.mufidgu.pastpapers.infrastructure.controller.university;

import com.mufidgu.pastpapers.domain.university.api.AddUniversity;
import com.mufidgu.pastpapers.domain.university.api.DeleteUniversity;
import com.mufidgu.pastpapers.domain.university.api.FetchUniversity;
import com.mufidgu.pastpapers.domain.university.api.UpdateUniversity;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Validated
@RestController
@RequestMapping("/university")
public class UniversityController {

    private final AddUniversity universityAdder;
    private final FetchUniversity universityFetcher;
    private final UpdateUniversity universityUpdater;
    private final DeleteUniversity universityDeleter;

    public UniversityController(
            AddUniversity universityAdder,
            FetchUniversity universityFetcher,
            UpdateUniversity universityUpdater,
            DeleteUniversity universityDeleter) {
        this.universityAdder = universityAdder;
        this.universityFetcher = universityFetcher;
        this.universityUpdater = universityUpdater;
        this.universityDeleter = universityDeleter;
    }

    // TODO: Admin Only
    // Test Cases: Validation, Duplicate Short Name and Full Name,
    @PostMapping("/add")
    public ResponseEntity<UniversityResource> addUniversity(@Valid @RequestBody UniversityRequest request) {
        var university = universityAdder.add(request.shortName, request.fullName);
        return ResponseEntity.ok(new UniversityResource(
                university.id(),
                university.shortName(),
                university.fullName()
        ));
    }

    // TODO: Registered Users Only
    @GetMapping("/all")
    public ResponseEntity<Iterable<UniversityResource>> getAllUniversities() {
        var universities = universityFetcher.fetchAll();
        return ResponseEntity.ok(universities.stream()
                .map(u -> new UniversityResource(u.id(), u.shortName(), u.fullName()))
                .toList());
    }

    // TODO: Admin Only
    // Test Cases: Validation, Duplicate Short Name and Full Name,
    @PutMapping("/update")
    public ResponseEntity<UniversityResource> updateUniversity(
            @RequestParam @NotBlank String universityId,
            @Valid @RequestBody UniversityRequest request
    ) {
        var id = UUID.fromString(universityId);
        var university = universityUpdater.update(id, request.shortName, request.fullName);
        return ResponseEntity.ok(new UniversityResource(
                university.id(),
                university.shortName(),
                university.fullName()
        ));
    }

    // TODO: Admin Only
    // Test Cases: Validation, University Not Found
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteUniversity(@RequestParam @NotBlank String universityId) {
        var id = UUID.fromString(universityId);
        universityDeleter.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
