package com.mufidgu.pastpapers.domain.instructor;

import com.mufidgu.pastpapers.domain.instructor.spi.Instructors;
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
class InstructorListerTest {

    @Mock
    private Instructors instructors;

    @InjectMocks
    private InstructorLister instructorLister;

    @Test
    void shouldReturnAllInstructors() {
        // Given
        Instructor instructor1 = new Instructor("Dr. John Smith", List.of(), List.of());
        Instructor instructor2 = new Instructor("Dr. Jane Doe", List.of(), List.of());
        List<Instructor> expectedInstructors = List.of(instructor1, instructor2);

        when(instructors.findAll()).thenReturn(expectedInstructors);

        // When
        List<Instructor> result = instructorLister.listAll();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedInstructors, result);
        verify(instructors).findAll();
    }

    @Test
    void shouldReturnEmptyListWhenNoInstructors() {
        // Given
        when(instructors.findAll()).thenReturn(Collections.emptyList());

        // When
        List<Instructor> result = instructorLister.listAll();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(instructors).findAll();
    }
}
