package com.mufidgu.pastpapers.infrastructure.controller.degree;

import com.mufidgu.pastpapers.domain.degree.Degree;
import com.mufidgu.pastpapers.domain.degree.api.AddDegree;
import com.mufidgu.pastpapers.domain.degree.api.DeleteDegree;
import com.mufidgu.pastpapers.domain.degree.api.ListDegree;
import com.mufidgu.pastpapers.domain.degree.api.UpdateDegree;
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
@RequestMapping("/degree")
public class DegreeController {

    private final AddDegree degreeAdder;
    private final ListDegree degreeLister;
    private final UpdateDegree degreeUpdater;
    private final DeleteDegree degreeDeleter;

    // Test cases validation, Already Exists, Institution Does Not Exist
    @PostMapping("/add")
    public ResponseEntity<DegreeResource> add(@Valid @RequestBody DegreeRequest request) {
        Degree degree = degreeAdder.add(request.shortName, request.fullName, request.institutionIds);
        return ResponseEntity.ok(
                DegreeResource.from(degree)
        );
    }

    // Test cases validation, Degree/Institution Does Not Exist
    @PostMapping("/update")
    public ResponseEntity<DegreeResource> update(
            @RequestParam @NotBlank String degreeId,
            @Valid @RequestBody DegreeRequest request
    ) {
        UUID id = UUID.fromString(degreeId);
        Degree degree = degreeUpdater.update(
                id,
                request.shortName,
                request.fullName,
                request.institutionIds
        );
        return ResponseEntity.ok(
                DegreeResource.from(degree)
        );
    }

    // Test cases validation, Degree Does Not Exist
    @PostMapping("/delete")
    public ResponseEntity<Void> delete(@RequestParam @NotBlank String degreeId) {
        UUID id = UUID.fromString(degreeId);
        degreeDeleter.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/list")
    public ResponseEntity<List<DegreeResource>> list() {
        List<Degree> degrees = degreeLister.listAll();
        return ResponseEntity.ok(degrees.stream()
                .map(DegreeResource::from)
                .toList());
    }
}
