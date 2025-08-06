package com.mufidgu.pastpapers.domain.degree;

import com.mufidgu.pastpapers.domain.degree.api.DeleteDegree;
import com.mufidgu.pastpapers.domain.degree.spi.Degrees;
import ddd.DomainService;

import java.util.UUID;

@DomainService
public class DegreeDeleter implements DeleteDegree {

    private final Degrees degrees;

    public DegreeDeleter(Degrees degrees) {
        this.degrees = degrees;
    }

    public void delete(UUID id) {
        // TODO: better exception handling
        degrees.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Degree does not exist"));

        degrees.delete(id);
    }
}
