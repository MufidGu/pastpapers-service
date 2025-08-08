package com.mufidgu.pastpapers.infrastructure.controller.instructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;

import java.util.List;
import java.util.UUID;

public class InstructorRequest {
    @NotBlank
    @Length(min = 2, max = 30)
    public String fullName;
    public List<UUID> courseIds;
    public List<UUID> universityIds;
}
