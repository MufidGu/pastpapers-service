package com.mufidgu.pastpapers.domain.institution;

import com.mufidgu.pastpapers.domain.institution.spi.Institutions;
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
class InstitutionListerTest {

    @Mock
    private Institutions institutions;

    @InjectMocks
    private InstitutionLister institutionLister;

    @Test
    void shouldReturnAllInstitutions() {
        // Given
        Institution institution1 = new Institution("MIT", "Massachusetts Institute of Technology");
        Institution institution2 = new Institution("Harvard", "Harvard University");
        List<Institution> expectedInstitutions = List.of(institution1, institution2);

        when(institutions.findAll()).thenReturn(expectedInstitutions);

        // When
        List<Institution> result = institutionLister.listAll();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedInstitutions, result);
        verify(institutions).findAll();
    }

    @Test
    void shouldReturnEmptyListWhenNoInstitutions() {
        // Given
        when(institutions.findAll()).thenReturn(Collections.emptyList());

        // When
        List<Institution> result = institutionLister.listAll();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(institutions).findAll();
    }
}
