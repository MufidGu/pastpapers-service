package com.mufidgu.pastpapers.domain.degree;

import com.mufidgu.pastpapers.domain.degree.api.ListDegree;
import com.mufidgu.pastpapers.domain.degree.spi.Degrees;
import ddd.DomainService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@DomainService
@RequiredArgsConstructor
public class DegreeLister implements ListDegree {

    private final Degrees degrees;

    public List<Degree> fetchAll() {
        return degrees.findAll();
    }
}
