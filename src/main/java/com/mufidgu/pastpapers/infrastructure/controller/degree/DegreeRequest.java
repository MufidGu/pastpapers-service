package com.mufidgu.pastpapers.infrastructure.controller.degree;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

import java.util.List;
import java.util.UUID;

public class DegreeRequest {
    @NotBlank
    @Length(min = 2, max = 30)
    public String shortName;

    @NotBlank
    @Length(min = 3, max = 100)
    public String fullName;

    public List<UUID> universityIds;
}
