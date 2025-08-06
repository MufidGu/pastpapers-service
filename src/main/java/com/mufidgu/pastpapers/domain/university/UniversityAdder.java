package com.mufidgu.pastpapers.domain.university;

import com.mufidgu.pastpapers.domain.university.api.AddUniversity;
import com.mufidgu.pastpapers.domain.university.spi.Universities;
import ddd.DomainService;

@DomainService
public class UniversityAdder implements AddUniversity {

    private final Universities universities;

    public UniversityAdder(Universities universities) {
        this.universities = universities;
    }

    public University add(String shortName, String fullName) {
        universities.findByShortNameAndFullName(shortName, fullName).ifPresent(u -> {
            throw new IllegalArgumentException("University already exists."); // TODO: handle this properly
        });
        var university = new University(shortName, fullName);
        return universities.save(university);
    }

}
