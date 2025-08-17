package com.mufidgu.pastpapers.domain.degree;

import com.mufidgu.pastpapers.domain.degree.api.UpdateDegree;
import com.mufidgu.pastpapers.domain.degree.spi.Degrees;
import com.mufidgu.pastpapers.domain.institution.spi.Institutions;
import ddd.DomainService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@DomainService
@RequiredArgsConstructor
public class DegreeUpdater implements UpdateDegree {

    private final Degrees degrees;
    private final Institutions institutions;

    public Degree update(UUID id, String shortName, String fullName, List<UUID> institutions) {
        // TODO: better exception handling
        Degree degree = degrees.findById(id).orElseThrow(() -> new IllegalArgumentException("Degree does not exist"));
        degrees.findByShortNameAndFullName(shortName, fullName).ifPresent(d -> {
            if (!d.id().equals(degree.id())) {
                throw new IllegalArgumentException("Degree with the same short name and full name already exists");
            }
        });
        institutions.forEach(institutionId -> {
            if (this.institutions.findById(institutionId) == null) {
                throw new IllegalArgumentException("Institution not found with id: " + institutionId);
            }
        });

        // TODO: Revisit this when adding database to project
        return degrees.save(
                new Degree(degree.id(), shortName, fullName, institutions)
        );
    }
}
