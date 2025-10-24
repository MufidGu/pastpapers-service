package com.mufidgu.pastpapers.domain.degree;

import com.mufidgu.pastpapers.domain.degree.spi.Degrees;
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
class DegreeListerTest {

    @Mock
    private Degrees degrees;

    @InjectMocks
    private DegreeLister degreeLister;

    @Test
    void shouldReturnAllDegrees() {
        // Given
        Degree degree1 = new Degree("CS", "Computer Science", List.of());
        Degree degree2 = new Degree("EE", "Electrical Engineering", List.of());
        List<Degree> expectedDegrees = List.of(degree1, degree2);

        when(degrees.findAll()).thenReturn(expectedDegrees);

        // When
        List<Degree> result = degreeLister.listAll();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedDegrees, result);
        verify(degrees).findAll();
    }

    @Test
    void shouldReturnEmptyListWhenNoDegrees() {
        // Given
        when(degrees.findAll()).thenReturn(Collections.emptyList());

        // When
        List<Degree> result = degreeLister.listAll();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(degrees).findAll();
    }
}
