package com.mufidgu.pastpapers.domain.paper.spi.stub;

import com.mufidgu.pastpapers.domain.paper.Paper;
import com.mufidgu.pastpapers.domain.paper.spi.Papers;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class InMemoryPapers implements Papers {

    HashMap<UUID, Paper> papers = new HashMap<>();

    public Paper save(Paper paper) {
        papers.put(paper.id(), paper);
        return paper;
    }

    public Optional<Paper> findById(UUID id) {
        return Optional.ofNullable(papers.get(id));
    }

    public void deleteById(UUID id) {
        papers.remove(id);
    }

    public Paper update(Paper paper) {
        papers.put(paper.id(), paper);
        return paper;
    }
}
