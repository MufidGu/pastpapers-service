package com.mufidgu.pastpapers.domain.paper.api;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface UploadPaper {
    UUID uploadPaper(byte[] file);
}
