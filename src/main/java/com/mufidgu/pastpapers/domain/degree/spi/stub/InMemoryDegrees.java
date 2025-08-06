package com.mufidgu.pastpapers.domain.degree.spi.stub;

import com.mufidgu.pastpapers.domain.degree.Degree;
import com.mufidgu.pastpapers.domain.degree.spi.Degrees;
import ddd.Stub;

import java.util.*;

@Stub
public class InMemoryDegrees implements Degrees {

    HashMap<UUID, Degree> degrees = new HashMap<>();

    public Degree save(Degree degree) {
        degrees.put(degree.id(), degree);
        return degree;
    }

    public Optional<Degree> findByShortNameAndFullName(String shortName, String fullName) {
        return degrees.values().stream()
                .filter(degree -> degree.shortName().equals(shortName) && degree.fullName().equals(fullName))
                .findFirst();
    }

    public Optional<Degree> findById(UUID id) {
        return Optional.ofNullable(degrees.get(id));
    }

    public void delete(UUID id) {
        degrees.remove(id);
    }

    public List<Degree> findAll() {
        return List.copyOf(degrees.values());
    }

}
