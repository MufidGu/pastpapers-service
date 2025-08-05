package com.mufidgu.pastpapers.infrastructure.controller.university;

import com.mufidgu.pastpapers.domain.university.api.AddUniversity;
import com.mufidgu.pastpapers.domain.university.api.FetchUniversities;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/university")
public class UniversityController {

    private final AddUniversity universityAdder;
    private final FetchUniversities universityFetcher;

    public UniversityController(AddUniversity universityAdder, FetchUniversities universityFetcher) {
        this.universityAdder = universityAdder;
        this.universityFetcher = universityFetcher;
    }

    // TODO: Admin Only
    // Test Cases: Validation, Duplicate Short Name, Duplicate Full Name,
    @PostMapping("/add")
    public ResponseEntity<UniversityResource> addUniversity(@RequestBody AddUniversityRequest request) {
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
}
