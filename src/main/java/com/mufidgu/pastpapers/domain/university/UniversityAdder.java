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

    public University addUniversity(String shortName, String fullName) {
        var university = new University(shortName, fullName);
        if (universities.findByShortNameAndFullName(shortName, fullName) != null) {
            throw new IllegalArgumentException("University already exists."); // TODO: handle this properly
        }
        return universities.save(university);
    }

}
