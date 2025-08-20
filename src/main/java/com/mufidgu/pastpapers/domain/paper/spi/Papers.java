package com.mufidgu.pastpapers.domain.paper.spi;

import com.mufidgu.pastpapers.domain.paper.Paper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface Papers {
    Paper save(Paper paper);

    Optional<Paper> findById(UUID id);

    void delete(UUID id);

    List<Paper> findAll();

    Optional<Paper>findByIdAndUserId(UUID id, String userId);
}
