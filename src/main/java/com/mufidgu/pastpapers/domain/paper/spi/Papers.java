package com.mufidgu.pastpapers.domain.paper.spi;

import com.mufidgu.pastpapers.domain.paper.Paper;

import java.util.Optional;
import java.util.UUID;

public interface Papers {
    Paper save(Paper paper);
    Optional<Paper> findById(UUID id);
    void deleteById(UUID id);
    Paper update(Paper paper);
}
