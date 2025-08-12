package com.mufidgu.pastpapers.domain.paper;

import com.mufidgu.pastpapers.domain.paper.api.DownloadPaper;
import ddd.DomainService;

import java.util.UUID;

@DomainService
public class PaperDownloader implements DownloadPaper {
    public byte[] downloadPaper(UUID id) {
        return new byte[0];
    }
}
