package com.mufidgu.pastpapers.domain.instructor;

import com.mufidgu.pastpapers.domain.instructor.api.DeleteInstructor;
import com.mufidgu.pastpapers.domain.instructor.spi.Instructors;
import ddd.DomainService;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@DomainService
@RequiredArgsConstructor
public class InstructorDeleter implements DeleteInstructor {

    private final Instructors instructors;

    @Override
    public void delete(UUID instructorId) {
        instructors.findById(instructorId)
                .orElseThrow(() -> new IllegalArgumentException("Instructor does not exist"));
        instructors.delete(instructorId);
    }
}
