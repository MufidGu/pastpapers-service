package com.mufidgu.pastpapers.domain.institution.spi.stub;

import com.mufidgu.pastpapers.domain.institution.Institution;
import com.mufidgu.pastpapers.domain.institution.spi.Institutions;
import ddd.Stub;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Stub
public class InMemoryInstitutions implements Institutions {

    private final HashMap<UUID, Institution> institutions = new HashMap<>();

    public Institution save(Institution institution) {
        institutions.put(institution.id(), institution);
        return institution;
    }

    public Optional<Institution> findByShortNameAndFullName(String shortName, String fullName) {
        return institutions.values().stream()
                .filter(u -> u.shortName().equals(shortName) && u.fullName().equals(fullName))
                .findFirst();
    }

    public List<Institution> findAll() {
        return List.copyOf(institutions.values());
    }

    public Optional<Institution> findById(UUID id) {
        return Optional.ofNullable(institutions.get(id));
    }

    public void delete(UUID id) {
        institutions.remove(id);
    }
}
