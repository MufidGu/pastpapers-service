package com.mufidgu.pastpapers.domain.instructor;

import com.mufidgu.pastpapers.domain.instructor.api.UpdateInstructor;
import com.mufidgu.pastpapers.domain.instructor.spi.Instructors;
import com.mufidgu.pastpapers.domain.course.spi.Courses;
import com.mufidgu.pastpapers.domain.institution.spi.Institutions;
import com.mufidgu.pastpapers.domain.common.exception.ConflictException;
import com.mufidgu.pastpapers.domain.common.exception.NotFoundException;
import ddd.DomainService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@DomainService
@RequiredArgsConstructor
public class InstructorUpdater implements UpdateInstructor {

    private final Instructors instructors;
    private final Courses courses;
    private final Institutions institutions;

    @Override
    public Instructor update(UUID id, String fullName, List<UUID> courseIds, List<UUID> institutionIds) {
        Instructor existingInstructor = instructors.findById(id)
                .orElseThrow(() -> new NotFoundException("Instructor does not exist"));

        instructors.findByFullName(fullName)
                .ifPresent(instructor -> {
                    if (!instructor.id().equals(existingInstructor.id())) {
                        throw new ConflictException("Instructor with same full name already exists");
                    }
                });

        courseIds.forEach(courseId -> {
            if (courses.findById(courseId).isEmpty()) {
                throw new NotFoundException("Course with ID " + courseId + " does not exist");
            }
        });

        institutionIds.forEach(institutionId -> {
            if (institutions.findById(institutionId).isEmpty()) {
                throw new NotFoundException("Institution with ID " + institutionId + " does not exist");
            }
        });

        return instructors.save(
                new Instructor(
                        existingInstructor.id(),
                        fullName,
                        courseIds,
                        institutionIds
                )
        );
    }
}
