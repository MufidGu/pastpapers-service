package com.mufidgu.pastpapers.domain.instructor;

import com.mufidgu.pastpapers.domain.instructor.api.FetchInstructor;
import com.mufidgu.pastpapers.domain.instructor.spi.Instructors;
import ddd.DomainService;

import java.util.List;

@DomainService
public class InstructorFetcher implements FetchInstructor {

    private final Instructors instructors;

    public InstructorFetcher(Instructors instructors) {
        this.instructors = instructors;
    }

    @Override
    public List<Instructor> fetchAll() {
        return instructors.findAll();
    }
}
