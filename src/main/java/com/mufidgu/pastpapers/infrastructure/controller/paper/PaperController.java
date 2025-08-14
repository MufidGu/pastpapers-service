package com.mufidgu.pastpapers.infrastructure.controller.paper;

import com.mufidgu.pastpapers.domain.paper.api.DownloadPaper;
import com.mufidgu.pastpapers.domain.paper.api.UploadPaper;
import com.mufidgu.pastpapers.infrastructure.annotation.FileTypeRestriction;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Validated
@RestController
@RequestMapping("/paper")
public class PaperController {

    private final UploadPaper paperUploader;
    private final DownloadPaper downloadPaper;

    public PaperController(UploadPaper paperUploader, DownloadPaper downloadPaper) {
        this.paperUploader = paperUploader;
        this.downloadPaper = downloadPaper;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> addPaper(
            @Valid
            @FileTypeRestriction(
                    acceptedTypes =  {
                            MediaType.TEXT_PLAIN_VALUE,
                            MediaType.APPLICATION_PDF_VALUE,
                            MediaType.IMAGE_JPEG_VALUE,
                            MediaType.IMAGE_PNG_VALUE
                    }
            )
            @RequestPart(name = "file") MultipartFile file
    ) {
        try {
            UUID paperId = paperUploader.uploadPaper(
                    file.getInputStream(),
                    file.getOriginalFilename()
            );
            return ResponseEntity.ok(paperId.toString());
        } catch (IOException e) {
            log.error("Error uploading paper: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Unknown error occurred while uploading the paper");
        }
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadPaper(
            @NotBlank @RequestParam String paperId
    ) {
        UUID id = UUID.fromString(paperId);
        return ResponseEntity.ok().body(
                downloadPaper.downloadPaper(id)
        );
    }
}
