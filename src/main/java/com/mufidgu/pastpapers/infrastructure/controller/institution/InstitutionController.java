package com.mufidgu.pastpapers.infrastructure.controller.institution;

import com.mufidgu.pastpapers.domain.institution.Institution;
import com.mufidgu.pastpapers.domain.institution.api.AddInstitution;
import com.mufidgu.pastpapers.domain.institution.api.DeleteInstitution;
import com.mufidgu.pastpapers.domain.institution.api.ListInstitution;
import com.mufidgu.pastpapers.domain.institution.api.UpdateInstitution;
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
@RequestMapping("/institution")
public class InstitutionController {

    private final AddInstitution institutionAdder;
    private final ListInstitution institutionLister;
    private final UpdateInstitution institutionUpdater;
    private final DeleteInstitution institutionDeleter;

    // Test Cases: Validation, Duplicate Short Name and Full Name,
    @PostMapping("/add")
    public ResponseEntity<InstitutionResource> add(@Valid @RequestBody InstitutionRequest request) {
        Institution institution = institutionAdder.add(request.shortName, request.fullName);
        return ResponseEntity.ok(new InstitutionResource(
                institution.id(),
                institution.shortName(),
                institution.fullName()
        ));
    }

    @GetMapping("/list")
    public ResponseEntity<Iterable<InstitutionResource>> list() {
        List<Institution> institutions = institutionLister.listAll();
        return ResponseEntity.ok(institutions.stream()
                .map(it -> new InstitutionResource(it.id(), it.shortName(), it.fullName()))
                .toList());
    }

    // Test Cases: Validation, Duplicate Short Name and Full Name,
    @PutMapping("/update")
    public ResponseEntity<InstitutionResource> update(
            @RequestParam @NotBlank String institutionId,
            @Valid @RequestBody InstitutionRequest request
    ) {
        UUID id = UUID.fromString(institutionId);
        Institution institution = institutionUpdater.update(id, request.shortName, request.fullName);
        return ResponseEntity.ok(new InstitutionResource(
                institution.id(),
                institution.shortName(),
                institution.fullName()
        ));
    }

    // Test Cases: Validation, Institution Not Found
    @DeleteMapping("/delete")
    public ResponseEntity<Void> delete(@RequestParam @NotBlank String institutionId) {
        UUID id = UUID.fromString(institutionId);
        institutionDeleter.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
