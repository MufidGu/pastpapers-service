package com.mufidgu.pastpapers.infrastructure.controller.university;

import com.mufidgu.pastpapers.domain.university.api.AddUniversity;
import com.mufidgu.pastpapers.domain.university.api.FetchUniversities;
import com.mufidgu.pastpapers.domain.university.api.UpdateUniversity;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/university")
public class UniversityController {

    private final AddUniversity universityAdder;
    private final FetchUniversities universityFetcher;
    private final UpdateUniversity universityUpdater;

    public UniversityController(
            AddUniversity universityAdder,
            FetchUniversities universityFetcher,
            UpdateUniversity universityUpdater
    ) {
        this.universityAdder = universityAdder;
        this.universityFetcher = universityFetcher;
        this.universityUpdater = universityUpdater;
    }

    // TODO: Admin Only
    // Test Cases: Validation, Duplicate Short Name and Full Name,
    @PostMapping("/add")
    public ResponseEntity<UniversityResource> addUniversity(@Valid @RequestBody UniversityRequest request) {
        var university = universityAdder.addUniversity(request.shortName, request.fullName);
        return ResponseEntity.ok(new UniversityResource(
                university.id(),
                university.shortName(),
                university.fullName()
        ));
    }

    // TODO: Registered Users Only
    @GetMapping("/all")
    public ResponseEntity<Iterable<UniversityResource>> getAllUniversities() {
        var universities = universityFetcher.fetchUniversities();
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
        var university = universityUpdater.updateUniversity(universityId, request.shortName, request.fullName);
        return ResponseEntity.ok(new UniversityResource(
                university.id(),
                university.shortName(),
                university.fullName()
        ));
    }
}
