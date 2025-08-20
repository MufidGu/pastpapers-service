package com.mufidgu.pastpapers.domain.degree;

import com.mufidgu.pastpapers.domain.common.exception.ConflictException;
import com.mufidgu.pastpapers.domain.common.exception.NotFoundException;
import com.mufidgu.pastpapers.domain.degree.api.AddDegree;
import com.mufidgu.pastpapers.domain.degree.spi.Degrees;
import com.mufidgu.pastpapers.domain.institution.spi.Institutions;
import ddd.DomainService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@DomainService
@RequiredArgsConstructor
public class DegreeAdder implements AddDegree {

    private final Degrees degrees;
    private final Institutions institutions;

    public Degree add(String shortName, String fullName, List<UUID> institutionIds) {
        degrees.findByShortNameAndFullName(shortName, fullName).ifPresent(d -> {
            throw new ConflictException("Degree with same short name and full name already exists");
        });
        institutionIds.forEach(institutionId -> institutions.findById(institutionId)
                .orElseThrow(() -> new NotFoundException("Institution with ID " + institutionId + " does not exist")));

        return degrees.save(
                new Degree(shortName, fullName, institutionIds)
        );
    }
}
