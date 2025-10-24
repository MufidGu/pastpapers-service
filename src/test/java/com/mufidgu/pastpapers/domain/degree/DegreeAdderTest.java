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
class DegreeAdderTest {

    @Mock
    private Degrees degrees;

    @Mock
    private Institutions institutions;

    @InjectMocks
    private DegreeAdder degreeAdder;

    private UUID institutionId;

    @BeforeEach
    void setUp() {
        institutionId = UUID.randomUUID();
    }

    @Test
    void shouldAddDegreeSuccessfully() {
        // Given
        String shortName = "CS";
        String fullName = "Computer Science";
        List<UUID> institutionIds = List.of(institutionId);

        when(degrees.findByShortNameAndFullName(shortName, fullName)).thenReturn(Optional.empty());
        when(institutions.findById(institutionId)).thenReturn(Optional.of(mock(com.mufidgu.pastpapers.domain.institution.Institution.class)));
        when(degrees.save(any(Degree.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Degree result = degreeAdder.add(shortName, fullName, institutionIds);

        // Then
        assertNotNull(result);
        assertEquals(shortName, result.shortName());
        assertEquals(fullName, result.fullName());
        assertEquals(institutionIds, result.institutions());
        assertNotNull(result.id());

        verify(degrees).findByShortNameAndFullName(shortName, fullName);
        verify(institutions).findById(institutionId);
        verify(degrees).save(any(Degree.class));
    }

    @Test
    void shouldThrowConflictExceptionWhenDegreeAlreadyExists() {
        // Given
        String shortName = "CS";
        String fullName = "Computer Science";
        List<UUID> institutionIds = List.of(institutionId);

        Degree existingDegree = new Degree(shortName, fullName, institutionIds);
        when(degrees.findByShortNameAndFullName(shortName, fullName)).thenReturn(Optional.of(existingDegree));

        // When & Then
        ConflictException exception = assertThrows(ConflictException.class, () ->
                degreeAdder.add(shortName, fullName, institutionIds)
        );

        assertEquals("Degree with same short name and full name already exists", exception.getMessage());
        verify(degrees).findByShortNameAndFullName(shortName, fullName);
        verify(institutions, never()).findById(any());
        verify(degrees, never()).save(any());
    }

    @Test
    void shouldThrowNotFoundExceptionWhenInstitutionDoesNotExist() {
        // Given
        String shortName = "CS";
        String fullName = "Computer Science";
        List<UUID> institutionIds = List.of(institutionId);

        when(degrees.findByShortNameAndFullName(shortName, fullName)).thenReturn(Optional.empty());
        when(institutions.findById(institutionId)).thenReturn(Optional.empty());

        // When & Then
        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                degreeAdder.add(shortName, fullName, institutionIds)
        );

        assertTrue(exception.getMessage().contains("Institution with ID"));
        assertTrue(exception.getMessage().contains("does not exist"));
        verify(degrees).findByShortNameAndFullName(shortName, fullName);
        verify(institutions).findById(institutionId);
        verify(degrees, never()).save(any());
    }

    @Test
    void shouldAddDegreeWithMultipleInstitutions() {
        // Given
        String shortName = "EE";
        String fullName = "Electrical Engineering";
        UUID institutionId2 = UUID.randomUUID();
        List<UUID> institutionIds = List.of(institutionId, institutionId2);

        when(degrees.findByShortNameAndFullName(shortName, fullName)).thenReturn(Optional.empty());
        when(institutions.findById(institutionId)).thenReturn(Optional.of(mock(com.mufidgu.pastpapers.domain.institution.Institution.class)));
        when(institutions.findById(institutionId2)).thenReturn(Optional.of(mock(com.mufidgu.pastpapers.domain.institution.Institution.class)));
        when(degrees.save(any(Degree.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Degree result = degreeAdder.add(shortName, fullName, institutionIds);

        // Then
        assertNotNull(result);
        assertEquals(2, result.institutions().size());
        verify(institutions).findById(institutionId);
        verify(institutions).findById(institutionId2);
    }
}
