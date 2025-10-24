package com.mufidgu.pastpapers.domain.paper;

import com.mufidgu.pastpapers.domain.common.enums.Season;
import com.mufidgu.pastpapers.domain.common.enums.Section;
import com.mufidgu.pastpapers.domain.common.enums.Shift;
import com.mufidgu.pastpapers.domain.common.enums.Type;
import com.mufidgu.pastpapers.domain.common.exception.NotFoundException;
import com.mufidgu.pastpapers.domain.paper.spi.Papers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaperUpdaterTest {

    @Mock
    private Papers papers;

    @InjectMocks
    private PaperUpdater paperUpdater;

    @Test
    void shouldUpdatePaperSuccessfully() {
        // Given
        UUID paperId = UUID.randomUUID();
        String userId = "user123";
        UUID instructorId = UUID.randomUUID();
        UUID courseId = UUID.randomUUID();
        UUID institutionId = UUID.randomUUID();
        UUID degreeId = UUID.randomUUID();

        Paper originalPaper = Paper.createFromFileName("test.pdf", userId);
        originalPaper = new Paper(
                paperId,
                originalPaper.userId(),
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                originalPaper.fileName()
        );

        Paper updateData = new Paper(
                paperId,
                userId,
                instructorId,
                courseId,
                Type.MIDTERM,
                institutionId,
                degreeId,
                Shift.MORNING,
                3,
                Section.A,
                2024,
                Season.FALL,
                LocalDate.now(),
                "test.pdf"
        );

        when(papers.findByIdAndUserId(paperId, userId)).thenReturn(Optional.of(originalPaper));
        when(papers.save(any(Paper.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Paper result = paperUpdater.update(updateData, userId);

        // Then
        assertNotNull(result);
        assertEquals(paperId, result.id());
        assertEquals(instructorId, result.instructorId());
        assertEquals(courseId, result.courseId());
        assertEquals(Type.MIDTERM, result.type());
        assertEquals(institutionId, result.institutionId());
        assertEquals(degreeId, result.degreeId());
        assertEquals(Shift.MORNING, result.shift());
        assertEquals(3, result.semester());
        assertEquals(Section.A, result.section());
        assertEquals(2024, result.year());
        assertEquals(Season.FALL, result.season());
        assertEquals("test.pdf", result.fileName());

        verify(papers).findByIdAndUserId(paperId, userId);
        verify(papers).save(any(Paper.class));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenPaperDoesNotExist() {
        // Given
        UUID paperId = UUID.randomUUID();
        String userId = "user123";

        Paper updateData = new Paper(
                paperId,
                userId,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                "test.pdf"
        );

        when(papers.findByIdAndUserId(paperId, userId)).thenReturn(Optional.empty());

        // When & Then
        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                paperUpdater.update(updateData, userId)
        );

        assertEquals("Paper does not exist", exception.getMessage());
        verify(papers).findByIdAndUserId(paperId, userId);
        verify(papers, never()).save(any());
    }
}
