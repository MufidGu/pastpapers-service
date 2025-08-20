package com.mufidgu.pastpapers.domain.degree;

import com.mufidgu.pastpapers.domain.common.exception.ConflictException;
import com.mufidgu.pastpapers.domain.common.exception.NotFoundException;
import com.mufidgu.pastpapers.domain.degree.api.UpdateDegree;
import com.mufidgu.pastpapers.domain.degree.spi.Degrees;
import com.mufidgu.pastpapers.domain.institution.spi.Institutions;
import ddd.DomainService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@DomainService
@RequiredArgsConstructor
public class DegreeUpdater implements UpdateDegree {

    private final Degrees degrees;
    private final Institutions institutions;

    public Degree update(UUID id, String shortName, String fullName, List<UUID> institutionIds) {
        Degree degree = degrees.findById(id).orElseThrow(() -> new NotFoundException("Degree does not exist"));
        degrees.findByShortNameAndFullName(shortName, fullName).ifPresent(d -> {
            if (!d.id().equals(degree.id())) {
                throw new ConflictException("Degree with same short name and full name already exists");
            }
        });
        institutionIds.forEach(institutionId -> institutions.findById(institutionId)
                .orElseThrow(() -> new NotFoundException("Institution with ID " + institutionId + " does not exist")));

        return degrees.save(
                new Degree(degree.id(), shortName, fullName, institutionIds)
        );
    }
}
