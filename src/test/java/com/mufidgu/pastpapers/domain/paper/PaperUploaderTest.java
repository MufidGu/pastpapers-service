package com.mufidgu.pastpapers.domain.paper;

import com.mufidgu.pastpapers.domain.common.exception.InternalServerException;
import com.mufidgu.pastpapers.domain.paper.spi.FileStorage;
import com.mufidgu.pastpapers.domain.paper.spi.Papers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaperUploaderTest {

    @Mock
    private Papers papers;

    @Mock
    private FileStorage fileStorage;

    @InjectMocks
    private PaperUploader paperUploader;

    @Test
    void shouldUploadPaperSuccessfully() throws IOException {
        // Given
        String originalFilename = "test-paper.pdf";
        String userId = "user123";
        byte[] fileContent = "test content".getBytes();
        InputStream stream = new ByteArrayInputStream(fileContent);

        doNothing().when(fileStorage).store(any(byte[].class), anyString());
        when(papers.save(any(Paper.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        UUID result = paperUploader.uploadPaper(stream, originalFilename, userId);

        // Then
        assertNotNull(result);
        verify(fileStorage).store(any(byte[].class), anyString());
        verify(papers).save(any(Paper.class));
    }

    @Test
    void shouldThrowInternalServerExceptionOnIOException() throws IOException {
        // Given
        String originalFilename = "test-paper.pdf";
        String userId = "user123";
        InputStream stream = mock(InputStream.class);

        when(stream.readAllBytes()).thenThrow(new IOException("Read error"));

        // When & Then
        InternalServerException exception = assertThrows(InternalServerException.class, () ->
                paperUploader.uploadPaper(stream, originalFilename, userId)
        );

        assertEquals("Unknown error occurred while handling file upload", exception.getMessage());
        verify(papers, never()).save(any());
    }
}
