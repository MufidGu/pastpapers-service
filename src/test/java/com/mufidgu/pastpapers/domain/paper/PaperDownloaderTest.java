package com.mufidgu.pastpapers.domain.paper;

import com.mufidgu.pastpapers.domain.common.exception.InternalServerException;
import com.mufidgu.pastpapers.domain.common.exception.NotFoundException;
import com.mufidgu.pastpapers.domain.paper.spi.FileStorage;
import com.mufidgu.pastpapers.domain.paper.spi.Papers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaperDownloaderTest {

    @Mock
    private Papers papers;

    @Mock
    private FileStorage fileStorage;

    @InjectMocks
    private PaperDownloader paperDownloader;

    @Test
    void shouldDownloadPaperSuccessfully() throws IOException {
        // Given
        UUID paperId = UUID.randomUUID();
        String filename = "test-paper.pdf";
        byte[] fileContent = "test content".getBytes();

        Paper paper = Paper.createFromFileName(filename, "user123");
        paper = new Paper(
                paperId,
                paper.userId(),
                paper.instructorId(),
                paper.courseId(),
                paper.type(),
                paper.institutionId(),
                paper.degreeId(),
                paper.shift(),
                paper.semester(),
                paper.section(),
                paper.year(),
                paper.season(),
                paper.date(),
                paper.fileName()
        );

        when(papers.findById(paperId)).thenReturn(Optional.of(paper));
        when(fileStorage.retrieve(paperId.toString())).thenReturn(fileContent);

        // When
        File result = paperDownloader.downloadPaper(paperId);

        // Then
        assertNotNull(result);
        assertEquals(filename, result.filename());
        assertArrayEquals(fileContent, result.contents());

        verify(papers).findById(paperId);
        verify(fileStorage).retrieve(paperId.toString());
    }

    @Test
    void shouldThrowNotFoundExceptionWhenPaperDoesNotExist() throws IOException {
        // Given
        UUID paperId = UUID.randomUUID();

        when(papers.findById(paperId)).thenReturn(Optional.empty());

        // When & Then
        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                paperDownloader.downloadPaper(paperId)
        );

        assertEquals("Paper does not exist", exception.getMessage());
        verify(papers).findById(paperId);
        verify(fileStorage, never()).retrieve(any());
    }

    @Test
    void shouldThrowInternalServerExceptionOnIOException() throws IOException {
        // Given
        UUID paperId = UUID.randomUUID();
        Paper paper = Paper.createFromFileName("test.pdf", "user123");
        paper = new Paper(
                paperId,
                paper.userId(),
                paper.instructorId(),
                paper.courseId(),
                paper.type(),
                paper.institutionId(),
                paper.degreeId(),
                paper.shift(),
                paper.semester(),
                paper.section(),
                paper.year(),
                paper.season(),
                paper.date(),
                paper.fileName()
        );

        when(papers.findById(paperId)).thenReturn(Optional.of(paper));
        when(fileStorage.retrieve(paperId.toString())).thenThrow(new IOException("Read error"));

        // When & Then
        InternalServerException exception = assertThrows(InternalServerException.class, () ->
                paperDownloader.downloadPaper(paperId)
        );

        assertEquals("Unknown error occurred while handling file download", exception.getMessage());
        verify(papers).findById(paperId);
        verify(fileStorage).retrieve(paperId.toString());
    }
}
