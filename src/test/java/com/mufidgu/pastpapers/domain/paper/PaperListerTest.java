package com.mufidgu.pastpapers.domain.paper;

import com.mufidgu.pastpapers.domain.paper.spi.Papers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaperListerTest {

    @Mock
    private Papers papers;

    @InjectMocks
    private PaperLister paperLister;

    @Test
    void shouldReturnAllPapers() {
        // Given
        Paper paper1 = Paper.createFromFileName("test1.pdf", "user1");
        Paper paper2 = Paper.createFromFileName("test2.pdf", "user2");
        List<Paper> expectedPapers = List.of(paper1, paper2);

        when(papers.findAll()).thenReturn(expectedPapers);

        // When
        List<Paper> result = paperLister.listAll();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedPapers, result);
        verify(papers).findAll();
    }

    @Test
    void shouldReturnEmptyListWhenNoPapers() {
        // Given
        when(papers.findAll()).thenReturn(Collections.emptyList());

        // When
        List<Paper> result = paperLister.listAll();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(papers).findAll();
    }
}
