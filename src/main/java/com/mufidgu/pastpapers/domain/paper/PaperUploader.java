package com.mufidgu.pastpapers.domain.paper;

import com.mufidgu.pastpapers.domain.paper.api.UploadPaper;
import com.mufidgu.pastpapers.domain.paper.spi.FileStorage;
import com.mufidgu.pastpapers.domain.paper.spi.Papers;
import ddd.DomainService;

import java.io.InputStream;
import java.util.UUID;

@DomainService
public class PaperUploader implements UploadPaper {

    private final Papers papers;
    private final FileStorage fileStorage;

    public PaperUploader(Papers papers, FileStorage fileStorage) {
        this.papers = papers;
        this.fileStorage = fileStorage;
    }

    public UUID uploadPaper(InputStream stream, String originalFilename) {
        try {
            Paper paper = new Paper(
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    originalFilename
            );

            fileStorage.store(
                    stream.readAllBytes(),
                    String.valueOf(paper.id())
            );

            paper = papers.save(paper);

            return paper.id();
        } catch (Exception e) {
            throw new RuntimeException("Unknown error occurred while uploading the paper", e);
        }
    }
}
