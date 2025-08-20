package com.mufidgu.pastpapers.domain.degree;

import com.mufidgu.pastpapers.domain.common.exception.NotFoundException;
import com.mufidgu.pastpapers.domain.degree.api.DeleteDegree;
import com.mufidgu.pastpapers.domain.degree.spi.Degrees;
import ddd.DomainService;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@DomainService
@RequiredArgsConstructor
public class DegreeDeleter implements DeleteDegree {

    private final Degrees degrees;

    public void delete(UUID id) {
        degrees.findById(id)
                .orElseThrow(() -> new NotFoundException("Degree does not exist"));

        degrees.delete(id);
    }
}
