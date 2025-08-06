package com.mufidgu.pastpapers.domain.university;

import com.mufidgu.pastpapers.domain.university.api.FetchUniversity;
import com.mufidgu.pastpapers.domain.university.spi.Universities;
import ddd.DomainService;

import java.util.List;

@DomainService
public class UniversityFetcher implements FetchUniversity {

    public UniversityFetcher(Universities universities) {
        this.universities = universities;
    }

    private final Universities universities;

    public List<University> fetchAll() {
        return universities.getAll();
    }
}
