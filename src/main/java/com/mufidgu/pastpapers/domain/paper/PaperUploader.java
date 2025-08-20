package com.mufidgu.pastpapers.domain.paper;

import com.mufidgu.pastpapers.domain.common.exception.InternalServerException;
import com.mufidgu.pastpapers.domain.paper.api.UploadPaper;
import com.mufidgu.pastpapers.domain.paper.spi.FileStorage;
import com.mufidgu.pastpapers.domain.paper.spi.Papers;
import ddd.DomainService;
import lombok.RequiredArgsConstructor;

import java.io.InputStream;
import java.util.UUID;

@DomainService
@RequiredArgsConstructor
public class PaperUploader implements UploadPaper {

    private final Papers papers;
    private final FileStorage fileStorage;

    public UUID uploadPaper(InputStream stream, String originalFilename, String userId) {
        try {
            Paper paper = Paper.createFromFileName(originalFilename, userId);

            fileStorage.store(
                    stream.readAllBytes(),
                    String.valueOf(paper.id())
            );

            paper = papers.save(paper);

            return paper.id();
        } catch (Exception e) {
            throw new InternalServerException("Unknown error occurred while handling file upload");
        }
    }
}
