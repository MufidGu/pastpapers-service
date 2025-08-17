package com.mufidgu.pastpapers.domain.university;

import com.mufidgu.pastpapers.domain.university.api.FetchUniversity;
import com.mufidgu.pastpapers.domain.university.spi.Universities;
import ddd.DomainService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@DomainService
@RequiredArgsConstructor
public class UniversityFetcher implements FetchUniversity {

    private final Universities universities;

    public List<University> fetchAll() {
        return universities.findAll();
    }
}
