package com.mufidgu.pastpapers.domain.paper;

import com.mufidgu.pastpapers.domain.common.exception.NotFoundException;
import com.mufidgu.pastpapers.domain.paper.spi.FileStorage;
import com.mufidgu.pastpapers.domain.paper.spi.Papers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaperDeleterTest {

    @Mock
    private Papers papers;

    @Mock
    private FileStorage fileStorage;

    @InjectMocks
    private PaperDeleter paperDeleter;

    @Test
    void shouldDeletePaperSuccessfully() {
        // Given
        UUID paperId = UUID.randomUUID();
        String userId = "user123";
        Paper paper = Paper.createFromFileName("test.pdf", userId);
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

        when(papers.findByIdAndUserId(paperId, userId)).thenReturn(Optional.of(paper));
        doNothing().when(fileStorage).delete(paperId.toString());
        doNothing().when(papers).delete(paperId);

        // When
        paperDeleter.delete(paperId, userId);

        // Then
        verify(papers).findByIdAndUserId(paperId, userId);
        verify(fileStorage).delete(paperId.toString());
        verify(papers).delete(paperId);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenPaperDoesNotExist() {
        // Given
        UUID paperId = UUID.randomUUID();
        String userId = "user123";

        when(papers.findByIdAndUserId(paperId, userId)).thenReturn(Optional.empty());

        // When & Then
        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                paperDeleter.delete(paperId, userId)
        );

        assertEquals("Paper does not exist", exception.getMessage());
        verify(papers).findByIdAndUserId(paperId, userId);
        verify(fileStorage, never()).delete(any());
        verify(papers, never()).delete(any());
    }

    @Test
    void shouldDeletePaperEvenWhenFileStorageFails() {
        // Given
        UUID paperId = UUID.randomUUID();
        String userId = "user123";
        Paper paper = Paper.createFromFileName("test.pdf", userId);
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

        when(papers.findByIdAndUserId(paperId, userId)).thenReturn(Optional.of(paper));
        doThrow(new RuntimeException("Storage error")).when(fileStorage).delete(paperId.toString());
        doNothing().when(papers).delete(paperId);

        // When
        paperDeleter.delete(paperId, userId);

        // Then - paper should still be deleted from DB even if file deletion fails
        verify(papers).findByIdAndUserId(paperId, userId);
        verify(fileStorage).delete(paperId.toString());
        verify(papers).delete(paperId);
    }
}
