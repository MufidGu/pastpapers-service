package com.mufidgu.pastpapers.domain.instructor.spi;

import com.mufidgu.pastpapers.domain.instructor.Instructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface Instructors {
    Instructor save(Instructor instructor);
    void delete(UUID id);
    Optional<Instructor> findById(UUID id);
    Optional<Instructor> findByFullName(String fullName);
    List<Instructor> findAll();
}
