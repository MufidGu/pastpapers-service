package com.mufidgu.pastpapers.domain.institution;

import com.mufidgu.pastpapers.domain.common.exception.NotFoundException;
import com.mufidgu.pastpapers.domain.institution.spi.Institutions;
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
class InstitutionDeleterTest {

    @Mock
    private Institutions institutions;

    @InjectMocks
    private InstitutionDeleter institutionDeleter;

    @Test
    void shouldDeleteInstitutionSuccessfully() {
        // Given
        UUID institutionId = UUID.randomUUID();
        Institution institution = new Institution(institutionId, "MIT", "Massachusetts Institute of Technology");

        when(institutions.findById(institutionId)).thenReturn(Optional.of(institution));
        doNothing().when(institutions).delete(institutionId);

        // When
        institutionDeleter.delete(institutionId);

        // Then
        verify(institutions).findById(institutionId);
        verify(institutions).delete(institutionId);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenInstitutionDoesNotExist() {
        // Given
        UUID institutionId = UUID.randomUUID();

        when(institutions.findById(institutionId)).thenReturn(Optional.empty());

        // When & Then
        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                institutionDeleter.delete(institutionId)
        );

        assertEquals("Institution does not exist", exception.getMessage());
        verify(institutions).findById(institutionId);
        verify(institutions, never()).delete(any());
    }
}
