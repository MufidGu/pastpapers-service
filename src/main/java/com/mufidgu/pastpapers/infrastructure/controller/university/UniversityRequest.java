package com.mufidgu.pastpapers.infrastructure.controller.university;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public class UniversityRequest {
    @NotBlank
    @Length(min = 3, max = 30)
    public String shortName;

    @NotBlank
    @Length(min = 3, max = 100)
    public String fullName;
}
