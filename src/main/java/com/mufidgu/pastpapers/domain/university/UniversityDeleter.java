package com.mufidgu.pastpapers.domain.university;

import com.mufidgu.pastpapers.domain.university.api.DeleteUniversity;
import com.mufidgu.pastpapers.domain.university.spi.Universities;
import ddd.DomainService;

import java.util.UUID;

@DomainService
public class UniversityDeleter implements DeleteUniversity {

    private final Universities universities;

    public UniversityDeleter(Universities universities) {
        this.universities = universities;
    }

    public void delete(UUID id) {
        universities.findById(id).orElseThrow(
                () -> new IllegalArgumentException("University does not exist.") // TODO: handle this properly
        );
        universities.delete(id);
    }
}
