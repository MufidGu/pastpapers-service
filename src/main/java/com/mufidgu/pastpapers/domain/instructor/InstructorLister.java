package com.mufidgu.pastpapers.domain.instructor;

import com.mufidgu.pastpapers.domain.instructor.api.ListInstructor;
import com.mufidgu.pastpapers.domain.instructor.spi.Instructors;
import ddd.DomainService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@DomainService
@RequiredArgsConstructor
public class InstructorLister implements ListInstructor {

    private final Instructors instructors;

    @Override
    public List<Instructor> listAll() {
        return instructors.findAll();
    }
}
