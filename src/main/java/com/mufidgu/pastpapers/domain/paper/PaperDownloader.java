package com.mufidgu.pastpapers.domain.paper;

import com.mufidgu.pastpapers.domain.common.exception.InternalServerException;
import com.mufidgu.pastpapers.domain.common.exception.NotFoundException;
import com.mufidgu.pastpapers.domain.paper.api.DownloadPaper;
import com.mufidgu.pastpapers.domain.paper.spi.FileStorage;
import com.mufidgu.pastpapers.domain.paper.spi.Papers;
import ddd.DomainService;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.UUID;

@DomainService
@RequiredArgsConstructor
public class PaperDownloader implements DownloadPaper {

    private final Papers papers;
    private final FileStorage fileStorage;

    public File downloadPaper(UUID id) {
        Paper paper = papers.findById(id)
                .orElseThrow(() -> new NotFoundException("Paper does not exist"));

        try {
            byte[] contents = fileStorage.retrieve(id.toString());

            return new File(
                    paper.fileName(),
                    contents
            );
        } catch (IOException e) {
            throw new InternalServerException("Unknown error occurred while handling file download");
        }
    }
}
