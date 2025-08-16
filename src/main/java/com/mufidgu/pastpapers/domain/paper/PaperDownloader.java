package com.mufidgu.pastpapers.domain.paper;

import com.mufidgu.pastpapers.domain.paper.api.DownloadPaper;
import com.mufidgu.pastpapers.domain.paper.spi.FileStorage;
import com.mufidgu.pastpapers.domain.paper.spi.Papers;
import ddd.DomainService;

import java.io.IOException;
import java.util.UUID;

@DomainService
public class PaperDownloader implements DownloadPaper {

    private final Papers papers;
    private final FileStorage fileStorage;

    public PaperDownloader(Papers papers, FileStorage fileStorage) {
        this.papers = papers;
        this.fileStorage = fileStorage;
    }

    public File downloadPaper(UUID id) {
        Paper paper = papers.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Paper does not exist"));

        try {
            byte[] contents = fileStorage.retrieve(id.toString());

            return new File(
                    paper.fileName(),
                    contents
            );
        } catch (IOException e) {
            throw new RuntimeException("Error retrieving file", e);
        }
    }
}
