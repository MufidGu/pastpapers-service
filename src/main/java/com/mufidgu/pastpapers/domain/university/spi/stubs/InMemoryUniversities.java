package com.mufidgu.pastpapers.domain.university.spi.stubs;

import com.mufidgu.pastpapers.domain.university.University;
import com.mufidgu.pastpapers.domain.university.spi.Universities;
import ddd.Stub;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Stub
public class InMemoryUniversities implements Universities {

    HashMap<UUID, University> universities = new HashMap<>();

    public University save(University university) {
        universities.put(university.id(), university);
        return university;
    }

    public Optional<University> findByShortNameAndFullName(String shortName, String fullName) {
        return universities.values().stream()
                .filter(u -> u.shortName().equals(shortName) && u.fullName().equals(fullName))
                .findFirst();
    }

    public List<University> getAll() {
        return List.copyOf(universities.values());
    }

    public Optional<University> findById(UUID id) {
        return Optional.ofNullable(universities.get(id));
    }

    public void delete(UUID id) {
        universities.remove(id);
    }
}
