package com.mufidgu.pastpapers.domain.degree;

import com.mufidgu.pastpapers.domain.degree.api.AddDegree;
import com.mufidgu.pastpapers.domain.degree.spi.Degrees;
import com.mufidgu.pastpapers.domain.university.spi.Universities;
import ddd.DomainService;

import java.util.List;
import java.util.UUID;

@DomainService
public class DegreeAdder implements AddDegree {

    private final Degrees degrees;
    private final Universities universities;

    public DegreeAdder(Degrees degrees, Universities universities) {
        this.degrees = degrees;
        this.universities = universities;
    }

    public Degree add(String shortName, String fullName, List<UUID> universities) {
        // TODO: better exception handling
        degrees.findByShortNameAndFullName(shortName, fullName).ifPresent(d -> {
            throw new IllegalArgumentException("Degree with the same short name and full name already exists");
        });
        universities.forEach(universityId -> {
            if (this.universities.findById(universityId) == null) {
                throw new IllegalArgumentException("University not found with id: " + universityId);
            }
        });

        Degree degree = new Degree(shortName, fullName, universities);
        return degrees.save(degree);
    }
}
