package com.mufidgu.pastpapers.infrastructure.controller.paper;

import com.mufidgu.pastpapers.domain.paper.File;
import com.mufidgu.pastpapers.domain.paper.Paper;
import com.mufidgu.pastpapers.domain.paper.api.*;
import com.mufidgu.pastpapers.infrastructure.annotation.FileTypeRestriction;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/paper")
public class PaperController {

    private final UploadPaper paperUploader;
    private final DownloadPaper paperDownloader;
    private final UpdatePaper paperUpdater;
    private final DeletePaper paperDeleter;
    private final ListPaper paperLister;

    @PostMapping("/upload")
    public ResponseEntity<String> add(
            @AuthenticationPrincipal Jwt jwt,
            @Valid
            @FileTypeRestriction(
                    acceptedTypes = {
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
                    file.getOriginalFilename(),
                    jwt.getSubject()
            );
            return ResponseEntity.ok(paperId.toString());
        } catch (IOException e) {
            log.error("Error uploading paper: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Unknown error occurred while uploading the paper");
        }
    }

    @GetMapping("/download")
    public ResponseEntity<File> download(
            @NotBlank @RequestParam String paperId
    ) {
        UUID id = UUID.fromString(paperId);
        return ResponseEntity.ok().body(
                paperDownloader.downloadPaper(id)
        );
    }

    @PutMapping("/update")
    public ResponseEntity<PaperResource> update(
            @AuthenticationPrincipal Jwt jwt,
            @NotBlank @RequestParam String paperId,
            @Valid @RequestBody PaperRequest request
    ) {
        UUID id = UUID.fromString(paperId);
        Paper paper = paperUpdater.update(
                request.toPaper(id),
                jwt.getSubject()
        );
        return ResponseEntity.ok(
                PaperResource.from(paper)
        );
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(
            @AuthenticationPrincipal Jwt jwt,
            @NotBlank @RequestParam String paperId
    ) {
        UUID id = UUID.fromString(paperId);
        paperDeleter.delete(id, jwt.getSubject());
        return ResponseEntity.ok("Paper deleted successfully");
    }


    @GetMapping("/list")
    public ResponseEntity<List<PaperResource>> list() {
        List<Paper> papers = paperLister.listAll();
        List<PaperResource> paperResources = papers.stream()
                .map(PaperResource::from)
                .toList();
        return ResponseEntity.ok(paperResources);
    }
}
