package com.mufidgu.pastpapers.infrastructure.controller.university;

import com.mufidgu.pastpapers.domain.university.api.AddUniversity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/university")
public class UniversityController {

    private final AddUniversity universityAdder;

    public UniversityController(AddUniversity universityAdder) {
        this.universityAdder = universityAdder;
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
}
