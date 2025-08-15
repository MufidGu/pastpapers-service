package com.mufidgu.pastpapers.domain.paper.spi.stub;

import com.mufidgu.pastpapers.domain.paper.Paper;
import com.mufidgu.pastpapers.domain.paper.spi.Papers;
import ddd.Stub;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Stub
public class InMemoryPapers implements Papers {

    HashMap<UUID, Paper> papers = new HashMap<>();

    public Paper save(Paper paper) {
        papers.put(paper.id(), paper);
        return paper;
    }

    public Optional<Paper> findById(UUID id) {
        return Optional.ofNullable(papers.get(id));
    }

    public void delete(UUID id) {
        papers.remove(id);
    }

    public Paper update(Paper paper) {
        papers.put(paper.id(), paper);
        return paper;
    }

    public List<Paper> findAll() {
        return papers.values().stream().toList();
    }
}
