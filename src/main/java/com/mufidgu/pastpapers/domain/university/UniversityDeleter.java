package com.mufidgu.pastpapers.domain.university;

import com.mufidgu.pastpapers.domain.university.api.DeleteUniversity;
import com.mufidgu.pastpapers.domain.university.spi.Universities;
import ddd.DomainService;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@DomainService
@RequiredArgsConstructor
public class UniversityDeleter implements DeleteUniversity {

    private final Universities universities;

    public void delete(UUID id) {
        universities.findById(id).orElseThrow(
                () -> new IllegalArgumentException("University does not exist.") // TODO: handle this properly
        );
        universities.delete(id);
    }
}
