package com.mufidgu.pastpapers.domain.instructor.spi.stub;

import com.mufidgu.pastpapers.domain.instructor.Instructor;
import com.mufidgu.pastpapers.domain.instructor.spi.Instructors;
import ddd.Stub;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Stub
public class InMemoryInstructors implements Instructors {

    private final HashMap<UUID, Instructor> instructors = new HashMap<>();

    @Override
    public Instructor save(Instructor instructor) {
        instructors.put(instructor.id(), instructor);
        return instructor;
    }

    @Override
    public void delete(UUID id) {
        instructors.remove(id);
    }

    @Override
    public Optional<Instructor> findById(UUID id) {
        return Optional.ofNullable(instructors.get(id));
    }

    @Override
    public Optional<Instructor> findByFullName(String fullName) {
        return instructors.values().stream()
                .filter(instructor -> instructor.fullName().equals(fullName))
                .findFirst();
    }

    @Override
    public List<Instructor> findAll() {
        return List.copyOf(instructors.values());
    }
}
