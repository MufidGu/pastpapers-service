package com.mufidgu.pastpapers.domain.degree;

import com.mufidgu.pastpapers.domain.degree.api.UpdateDegree;
import com.mufidgu.pastpapers.domain.degree.spi.Degrees;
import com.mufidgu.pastpapers.domain.university.spi.Universities;
import ddd.DomainService;

import java.util.List;
import java.util.UUID;

@DomainService
public class DegreeUpdater implements UpdateDegree {

    private final Degrees degrees;
    private final Universities universities;

    public DegreeUpdater(Degrees degrees, Universities universities) {
        this.degrees = degrees;
        this.universities = universities;
    }

    public Degree update(UUID id, String shortName, String fullName, List<UUID> universities) {
        // TODO: better exception handling
        Degree degree = degrees.findById(id).orElseThrow(() -> new IllegalArgumentException("Degree does not exist"));
        degrees.findByShortNameAndFullName(shortName, fullName).ifPresent(d -> {
            throw new IllegalArgumentException("Degree with the same short name and full name already exists");
        });
        universities.forEach(universityId -> {
            if (this.universities.findById(universityId) == null) {
                throw new IllegalArgumentException("University not found with id: " + universityId);
            }
        });

        // TODO: Revisit this when adding database to project
        return degrees.save(
                new Degree(degree.id(), shortName, fullName, universities)
        );
    }
}
