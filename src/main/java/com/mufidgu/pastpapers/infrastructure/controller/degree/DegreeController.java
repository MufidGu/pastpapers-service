package com.mufidgu.pastpapers.infrastructure.controller.degree;

import com.mufidgu.pastpapers.domain.degree.Degree;
import com.mufidgu.pastpapers.domain.degree.api.AddDegree;
import com.mufidgu.pastpapers.domain.degree.api.DeleteDegree;
import com.mufidgu.pastpapers.domain.degree.api.FetchDegree;
import com.mufidgu.pastpapers.domain.degree.api.UpdateDegree;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Validated
@RestController
@RequestMapping("/degree")
public class DegreeController {

    private final AddDegree degreeAdder;
    private final FetchDegree degreeFetcher;
    private final UpdateDegree degreeUpdater;
    private final DeleteDegree degreeDeleter;

    public DegreeController(
            AddDegree degreeAdder,
            FetchDegree degreeFetcher,
            UpdateDegree degreeUpdater,
            DeleteDegree degreeDeleter) {
        this.degreeAdder = degreeAdder;
        this.degreeFetcher = degreeFetcher;
        this.degreeUpdater = degreeUpdater;
        this.degreeDeleter = degreeDeleter;
    }

    // TODO: Admin only
    // Test cases validation, Already Exists, University Does Not Exist
    @PostMapping("/add")
    public ResponseEntity<DegreeResource> addDegree(@Valid @RequestBody DegreeRequest request) {
        Degree degree = degreeAdder.add(request.shortName, request.fullName, request.universityIds);
        return ResponseEntity.ok(new DegreeResource(
                degree.id(),
                degree.shortName(),
                degree.fullName(),
                degree.universities()
        ));
    }

    // TODO: Admin only
    // Test cases validation, Degree/University Does Not Exist
    @PostMapping("/update")
    public ResponseEntity<DegreeResource> updateDegree(
            @RequestParam @NotBlank String degreeId,
            @Valid @RequestBody DegreeRequest request
    ) {
        UUID id = UUID.fromString(degreeId);
        Degree degree = degreeUpdater.update(
                id,
                request.shortName,
                request.fullName,
                request.universityIds
        );
        return ResponseEntity.ok(new DegreeResource(
                degree.id(),
                degree.shortName(),
                degree.fullName(),
                degree.universities()
        ));
    }

    // TODO: Admin only
    // Test cases validation, Degree Does Not Exist
    @PostMapping("/delete")
    public ResponseEntity<Void> deleteDegree(@RequestParam @NotBlank String degreeId) {
        UUID id = UUID.fromString(degreeId);
        degreeDeleter.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // TODO: Registered users only
    @GetMapping("/all")
    public ResponseEntity<Iterable<DegreeResource>> getAllDegrees() {
        var degrees = degreeFetcher.fetchAll();
        return ResponseEntity.ok(degrees.stream()
                .map(d -> new DegreeResource(d.id(), d.shortName(), d.fullName(), d.universities()))
                .toList());
    }
}
