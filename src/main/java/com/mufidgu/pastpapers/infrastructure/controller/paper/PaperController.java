package com.mufidgu.pastpapers.infrastructure.controller.paper;

import com.mufidgu.pastpapers.domain.paper.File;
import com.mufidgu.pastpapers.domain.paper.Paper;
import com.mufidgu.pastpapers.domain.paper.api.*;
import com.mufidgu.pastpapers.infrastructure.annotation.FileTypeRestriction;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@Validated
@RestController
@RequestMapping("/paper")
public class PaperController {

    private final UploadPaper paperUploader;
    private final DownloadPaper paperDownloader;
    private final UpdatePaper paperUpdater;
    private final DeletePaper paperDeleter;
    private final ListPaper paperLister;

    public PaperController(
            UploadPaper paperUploader,
            DownloadPaper paperDownloader,
            UpdatePaper paperUpdater,
            DeletePaper paperDeleter,
            ListPaper paperLister
    ) {
        this.paperUploader = paperUploader;
        this.paperDownloader = paperDownloader;
        this.paperUpdater = paperUpdater;
        this.paperDeleter = paperDeleter;
        this.paperLister = paperLister;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> addPaper(
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
                    file.getOriginalFilename()
            );
            return ResponseEntity.ok(paperId.toString());
        } catch (IOException e) {
            log.error("Error uploading paper: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Unknown error occurred while uploading the paper");
        }
    }

    @GetMapping("/download")
    public ResponseEntity<File> downloadPaper(
            @NotBlank @RequestParam String paperId
    ) {
        UUID id = UUID.fromString(paperId);
        return ResponseEntity.ok().body(
                paperDownloader.downloadPaper(id)
        );
    }

    @PutMapping("/update")
    public ResponseEntity<PaperResource> updatePaper(
            @NotBlank @RequestParam String paperId,
            @Valid @RequestBody PaperRequest request
    ) {
        UUID id = UUID.fromString(paperId);
        Paper paper = paperUpdater.update(
                id,
                request.instructorId,
                request.courseId,
                request.term,
                request.universityId,
                request.degreeId,
                request.shift,
                request.semester,
                request.section,
                request.year,
                request.season,
                request.date
        );
        return ResponseEntity.ok(
                new PaperResource(
                        paper.id(),
                        paper.instructorId(),
                        paper.courseId(),
                        paper.term(),
                        paper.universityId(),
                        paper.degreeId(),
                        paper.shift(),
                        paper.semester(),
                        paper.section(),
                        paper.year(),
                        paper.season(),
                        paper.date()
                )
        );
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deletePaper(
            @NotBlank @RequestParam String paperId
    ) {
        UUID id = UUID.fromString(paperId);
        paperDeleter.delete(id);
        return ResponseEntity.ok("Paper deleted successfully");
    }


    @GetMapping("/list")
    public ResponseEntity<List<PaperResource>> listPapers() {
        List<Paper> papers = paperLister.listAll();
        List<PaperResource> paperResources = papers.stream()
                .map(paper -> new PaperResource(
                        paper.id(),
                        paper.instructorId(),
                        paper.courseId(),
                        paper.term(),
                        paper.universityId(),
                        paper.degreeId(),
                        paper.shift(),
                        paper.semester(),
                        paper.section(),
                        paper.year(),
                        paper.season(),
                        paper.date()
                )).toList();
        return ResponseEntity.ok(paperResources);
    }
}
