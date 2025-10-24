package com.mufidgu.pastpapers.domain.institution;

import com.mufidgu.pastpapers.domain.common.exception.ConflictException;
import com.mufidgu.pastpapers.domain.institution.spi.Institutions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InstitutionAdderTest {

    @Mock
    private Institutions institutions;

    @InjectMocks
    private InstitutionAdder institutionAdder;

    @Test
    void shouldAddInstitutionSuccessfully() {
        // Given
        String shortName = "MIT";
        String fullName = "Massachusetts Institute of Technology";

        when(institutions.findByShortNameAndFullName(shortName, fullName)).thenReturn(Optional.empty());
        when(institutions.save(any(Institution.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Institution result = institutionAdder.add(shortName, fullName);

        // Then
        assertNotNull(result);
        assertEquals(shortName, result.shortName());
        assertEquals(fullName, result.fullName());
        assertNotNull(result.id());

        verify(institutions).findByShortNameAndFullName(shortName, fullName);
        verify(institutions).save(any(Institution.class));
    }

    @Test
    void shouldThrowConflictExceptionWhenInstitutionAlreadyExists() {
        // Given
        String shortName = "MIT";
        String fullName = "Massachusetts Institute of Technology";

        Institution existingInstitution = new Institution(shortName, fullName);
        when(institutions.findByShortNameAndFullName(shortName, fullName)).thenReturn(Optional.of(existingInstitution));

        // When & Then
        ConflictException exception = assertThrows(ConflictException.class, () ->
                institutionAdder.add(shortName, fullName)
        );

        assertEquals("Institution with same short name and full name already exists", exception.getMessage());
        verify(institutions).findByShortNameAndFullName(shortName, fullName);
        verify(institutions, never()).save(any());
    }
}
