package com.mufidgu.pastpapers.domain.degree.spi;

import com.mufidgu.pastpapers.domain.degree.Degree;

import java.util.Optional;

public interface Degrees {
    Optional<Degree> addDegree(Degree degree);
}
