package com.mufidgu.pastpapers.domain.paper;

import com.mufidgu.pastpapers.domain.paper.api.UploadPaper;
import ddd.DomainService;

import java.util.UUID;

@DomainService
public class PaperUploader implements UploadPaper {
    public UUID uploadPaper(byte[] file) {
        return UUID.randomUUID();
    }
}
