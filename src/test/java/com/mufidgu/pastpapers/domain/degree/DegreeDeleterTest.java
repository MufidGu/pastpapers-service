package com.mufidgu.pastpapers.domain.degree;

import com.mufidgu.pastpapers.domain.common.exception.NotFoundException;
import com.mufidgu.pastpapers.domain.degree.spi.Degrees;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DegreeDeleterTest {

    @Mock
    private Degrees degrees;

    @InjectMocks
    private DegreeDeleter degreeDeleter;

    @Test
    void shouldDeleteDegreeSuccessfully() {
        // Given
        UUID degreeId = UUID.randomUUID();
        Degree degree = new Degree(degreeId, "CS", "Computer Science", List.of());

        when(degrees.findById(degreeId)).thenReturn(Optional.of(degree));
        doNothing().when(degrees).delete(degreeId);

        // When
        degreeDeleter.delete(degreeId);

        // Then
        verify(degrees).findById(degreeId);
        verify(degrees).delete(degreeId);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenDegreeDoesNotExist() {
        // Given
        UUID degreeId = UUID.randomUUID();

        when(degrees.findById(degreeId)).thenReturn(Optional.empty());

        // When & Then
        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                degreeDeleter.delete(degreeId)
        );

        assertEquals("Degree does not exist", exception.getMessage());
        verify(degrees).findById(degreeId);
        verify(degrees, never()).delete(any());
    }
}
