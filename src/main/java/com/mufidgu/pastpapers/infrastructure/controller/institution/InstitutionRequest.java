package com.mufidgu.pastpapers.infrastructure.controller.institution;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public class InstitutionRequest {
    @NotBlank
    @Length(min = 3, max = 30)
    public String shortName;

    @NotBlank
    @Length(min = 3, max = 100)
    public String fullName;
}
