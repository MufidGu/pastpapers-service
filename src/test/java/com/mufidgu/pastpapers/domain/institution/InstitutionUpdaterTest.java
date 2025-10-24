package com.mufidgu.pastpapers.domain.institution;

import com.mufidgu.pastpapers.domain.common.exception.ConflictException;
import com.mufidgu.pastpapers.domain.common.exception.NotFoundException;
import com.mufidgu.pastpapers.domain.institution.spi.Institutions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InstitutionUpdaterTest {

    @Mock
    private Institutions institutions;

    @InjectMocks
    private InstitutionUpdater institutionUpdater;

    private UUID institutionId;

    @BeforeEach
    void setUp() {
        institutionId = UUID.randomUUID();
    }

    @Test
    void shouldUpdateInstitutionSuccessfully() {
        // Given
        String newShortName = "MIT-Boston";
        String newFullName = "Massachusetts Institute of Technology Boston";

        Institution existingInstitution = new Institution(institutionId, "MIT", "Massachusetts Institute of Technology");

        when(institutions.findById(institutionId)).thenReturn(Optional.of(existingInstitution));
        when(institutions.findByShortNameAndFullName(newShortName, newFullName)).thenReturn(Optional.empty());
        when(institutions.save(any(Institution.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Institution result = institutionUpdater.update(institutionId, newShortName, newFullName);

        // Then
        assertNotNull(result);
        assertEquals(institutionId, result.id());
        assertEquals(newShortName, result.shortName());
        assertEquals(newFullName, result.fullName());

        verify(institutions).findById(institutionId);
        verify(institutions).findByShortNameAndFullName(newShortName, newFullName);
        verify(institutions).save(any(Institution.class));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenInstitutionDoesNotExist() {
        // Given
        String newShortName = "MIT-Boston";
        String newFullName = "Massachusetts Institute of Technology Boston";

        when(institutions.findById(institutionId)).thenReturn(Optional.empty());

        // When & Then
        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                institutionUpdater.update(institutionId, newShortName, newFullName)
        );

        assertEquals("Institution with same short name and full name already exists", exception.getMessage());
        verify(institutions).findById(institutionId);
        verify(institutions, never()).findByShortNameAndFullName(any(), any());
        verify(institutions, never()).save(any());
    }

    @Test
    void shouldThrowConflictExceptionWhenUpdatedNameExistsForDifferentInstitution() {
        // Given
        String newShortName = "Harvard";
        String newFullName = "Harvard University";

        Institution existingInstitution = new Institution(institutionId, "MIT", "Massachusetts Institute of Technology");
        UUID otherInstitutionId = UUID.randomUUID();
        Institution conflictingInstitution = new Institution(otherInstitutionId, newShortName, newFullName);

        when(institutions.findById(institutionId)).thenReturn(Optional.of(existingInstitution));
        when(institutions.findByShortNameAndFullName(newShortName, newFullName)).thenReturn(Optional.of(conflictingInstitution));

        // When & Then
        ConflictException exception = assertThrows(ConflictException.class, () ->
                institutionUpdater.update(institutionId, newShortName, newFullName)
        );

        assertEquals("Institution with same short name and full name already exists", exception.getMessage());
        verify(institutions).findById(institutionId);
        verify(institutions).findByShortNameAndFullName(newShortName, newFullName);
        verify(institutions, never()).save(any());
    }

    @Test
    void shouldAllowUpdateWithSameNameForSameInstitution() {
        // Given
        String sameName = "MIT";
        String sameFullName = "Massachusetts Institute of Technology";

        Institution existingInstitution = new Institution(institutionId, sameName, sameFullName);

        when(institutions.findById(institutionId)).thenReturn(Optional.of(existingInstitution));
        when(institutions.findByShortNameAndFullName(sameName, sameFullName)).thenReturn(Optional.of(existingInstitution));
        when(institutions.save(any(Institution.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Institution result = institutionUpdater.update(institutionId, sameName, sameFullName);

        // Then
        assertNotNull(result);
        assertEquals(institutionId, result.id());
        verify(institutions).save(any(Institution.class));
    }
}
