package com.mufidgu.pastpapers.domain.degree;

import com.mufidgu.pastpapers.domain.common.exception.ConflictException;
import com.mufidgu.pastpapers.domain.common.exception.NotFoundException;
import com.mufidgu.pastpapers.domain.degree.spi.Degrees;
import com.mufidgu.pastpapers.domain.institution.spi.Institutions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DegreeUpdaterTest {

    @Mock
    private Degrees degrees;

    @Mock
    private Institutions institutions;

    @InjectMocks
    private DegreeUpdater degreeUpdater;

    private UUID degreeId;
    private UUID institutionId;

    @BeforeEach
    void setUp() {
        degreeId = UUID.randomUUID();
        institutionId = UUID.randomUUID();
    }

    @Test
    void shouldUpdateDegreeSuccessfully() {
        // Given
        String newShortName = "CSE";
        String newFullName = "Computer Science and Engineering";
        List<UUID> newInstitutionIds = List.of(institutionId);

        Degree existingDegree = new Degree(degreeId, "CS", "Computer Science", List.of());

        when(degrees.findById(degreeId)).thenReturn(Optional.of(existingDegree));
        when(degrees.findByShortNameAndFullName(newShortName, newFullName)).thenReturn(Optional.empty());
        when(institutions.findById(institutionId)).thenReturn(Optional.of(mock(com.mufidgu.pastpapers.domain.institution.Institution.class)));
        when(degrees.save(any(Degree.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Degree result = degreeUpdater.update(degreeId, newShortName, newFullName, newInstitutionIds);

        // Then
        assertNotNull(result);
        assertEquals(degreeId, result.id());
        assertEquals(newShortName, result.shortName());
        assertEquals(newFullName, result.fullName());
        assertEquals(newInstitutionIds, result.institutions());

        verify(degrees).findById(degreeId);
        verify(degrees).findByShortNameAndFullName(newShortName, newFullName);
        verify(institutions).findById(institutionId);
        verify(degrees).save(any(Degree.class));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenDegreeDoesNotExist() {
        // Given
        String newShortName = "CSE";
        String newFullName = "Computer Science and Engineering";
        List<UUID> newInstitutionIds = List.of(institutionId);

        when(degrees.findById(degreeId)).thenReturn(Optional.empty());

        // When & Then
        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                degreeUpdater.update(degreeId, newShortName, newFullName, newInstitutionIds)
        );

        assertEquals("Degree does not exist", exception.getMessage());
        verify(degrees).findById(degreeId);
        verify(degrees, never()).findByShortNameAndFullName(any(), any());
        verify(institutions, never()).findById(any());
        verify(degrees, never()).save(any());
    }

    @Test
    void shouldThrowConflictExceptionWhenUpdatedNameExistsForDifferentDegree() {
        // Given
        String newShortName = "EE";
        String newFullName = "Electrical Engineering";
        List<UUID> newInstitutionIds = List.of(institutionId);

        Degree existingDegree = new Degree(degreeId, "CS", "Computer Science", List.of());
        UUID otherDegreeId = UUID.randomUUID();
        Degree conflictingDegree = new Degree(otherDegreeId, newShortName, newFullName, List.of());

        when(degrees.findById(degreeId)).thenReturn(Optional.of(existingDegree));
        when(degrees.findByShortNameAndFullName(newShortName, newFullName)).thenReturn(Optional.of(conflictingDegree));

        // When & Then
        ConflictException exception = assertThrows(ConflictException.class, () ->
                degreeUpdater.update(degreeId, newShortName, newFullName, newInstitutionIds)
        );

        assertEquals("Degree with same short name and full name already exists", exception.getMessage());
        verify(degrees).findById(degreeId);
        verify(degrees).findByShortNameAndFullName(newShortName, newFullName);
        verify(institutions, never()).findById(any());
        verify(degrees, never()).save(any());
    }

    @Test
    void shouldAllowUpdateWithSameNameForSameDegree() {
        // Given
        String sameName = "CS";
        String sameFullName = "Computer Science";
        List<UUID> newInstitutionIds = List.of(institutionId);

        Degree existingDegree = new Degree(degreeId, sameName, sameFullName, List.of());

        when(degrees.findById(degreeId)).thenReturn(Optional.of(existingDegree));
        when(degrees.findByShortNameAndFullName(sameName, sameFullName)).thenReturn(Optional.of(existingDegree));
        when(institutions.findById(institutionId)).thenReturn(Optional.of(mock(com.mufidgu.pastpapers.domain.institution.Institution.class)));
        when(degrees.save(any(Degree.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Degree result = degreeUpdater.update(degreeId, sameName, sameFullName, newInstitutionIds);

        // Then
        assertNotNull(result);
        assertEquals(degreeId, result.id());
        verify(degrees).save(any(Degree.class));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenInstitutionDoesNotExist() {
        // Given
        String newShortName = "CSE";
        String newFullName = "Computer Science and Engineering";
        List<UUID> newInstitutionIds = List.of(institutionId);

        Degree existingDegree = new Degree(degreeId, "CS", "Computer Science", List.of());

        when(degrees.findById(degreeId)).thenReturn(Optional.of(existingDegree));
        when(degrees.findByShortNameAndFullName(newShortName, newFullName)).thenReturn(Optional.empty());
        when(institutions.findById(institutionId)).thenReturn(Optional.empty());

        // When & Then
        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                degreeUpdater.update(degreeId, newShortName, newFullName, newInstitutionIds)
        );

        assertTrue(exception.getMessage().contains("Institution with ID"));
        assertTrue(exception.getMessage().contains("does not exist"));
        verify(degrees).findById(degreeId);
        verify(institutions).findById(institutionId);
        verify(degrees, never()).save(any());
    }
}
