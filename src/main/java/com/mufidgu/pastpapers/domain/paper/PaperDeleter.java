package com.mufidgu.pastpapers.domain.paper;

import com.mufidgu.pastpapers.domain.paper.api.DeletePaper;
import com.mufidgu.pastpapers.domain.paper.spi.FileStorage;
import com.mufidgu.pastpapers.domain.paper.spi.Papers;
import ddd.DomainService;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
@DomainService
public class PaperDeleter implements DeletePaper {

    private final Papers papers;
    private final FileStorage fileStorage;

    public PaperDeleter(Papers papers, FileStorage fileStorage) {
        this.papers = papers;
        this.fileStorage = fileStorage;
    }

    public void delete(UUID id) {
        Paper paper = papers.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Paper does not exist"));

        try {
            fileStorage.delete(paper.id().toString());
        } catch (Exception e) {
            log.error("Failed to delete file for paper {}: {}", paper, e.getMessage());
        }

        papers.delete(paper.id());
    }
}
