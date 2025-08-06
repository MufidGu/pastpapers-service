package com.mufidgu.pastpapers.domain.degree;

import com.mufidgu.pastpapers.domain.degree.api.FetchDegree;
import com.mufidgu.pastpapers.domain.degree.spi.Degrees;
import ddd.DomainService;

import java.util.List;

@DomainService
public class DegreeFetcher implements FetchDegree {

    private final Degrees degrees;

    public DegreeFetcher(Degrees degrees) {
        this.degrees = degrees;
    }

    public List<Degree> fetchAll() {
        return degrees.findAll();
    }
}
