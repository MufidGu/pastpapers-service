package com.mufidgu.pastpapers.domain.university;

import com.mufidgu.pastpapers.domain.university.api.UpdateUniversity;
import com.mufidgu.pastpapers.domain.university.spi.Universities;
import ddd.DomainService;

import java.util.UUID;

@DomainService
public class UniversityUpdater implements UpdateUniversity {

    private final Universities universities;

    public UniversityUpdater(Universities universities) {
        this.universities = universities;
    }

    public University update(UUID id, String shortName, String fullName) {
        // TODO: better exception handling
        if (universities.findById(id.toString()) == null) {
            throw new IllegalArgumentException("University not found with id: " + id);
        }
        if (universities.findByShortNameAndFullName(shortName, fullName) != null) {
            throw new IllegalArgumentException("University with the same short name and full name already exists");
        }

        // TODO: Revisit this when adding database to project
        University university = new University(id, shortName, fullName);
        universities.save(university);

        return university;
    }
}
