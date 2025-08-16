package com.mufidgu.pastpapers.domain.paper.api;


import java.io.InputStream;
import java.util.UUID;

public interface UploadPaper {
    UUID uploadPaper(InputStream file, String originalFilename);
}
