package com.mufidgu.pastpapers.domain.instructor;

import com.mufidgu.pastpapers.domain.instructor.api.FetchInstructor;
import com.mufidgu.pastpapers.domain.instructor.spi.Instructors;
import ddd.DomainService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@DomainService
@RequiredArgsConstructor
public class InstructorFetcher implements FetchInstructor {

    private final Instructors instructors;

    @Override
    public List<Instructor> fetchAll() {
        return instructors.findAll();
    }
}
