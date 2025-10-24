package com.mufidgu.pastpapers.domain.instructor;

import com.mufidgu.pastpapers.domain.common.exception.NotFoundException;
import com.mufidgu.pastpapers.domain.instructor.spi.Instructors;
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
class InstructorDeleterTest {

    @Mock
    private Instructors instructors;

    @InjectMocks
    private InstructorDeleter instructorDeleter;

    @Test
    void shouldDeleteInstructorSuccessfully() {
        // Given
        UUID instructorId = UUID.randomUUID();
        Instructor instructor = new Instructor(instructorId, "Dr. John Smith", List.of(), List.of());

        when(instructors.findById(instructorId)).thenReturn(Optional.of(instructor));
        doNothing().when(instructors).delete(instructorId);

        // When
        instructorDeleter.delete(instructorId);

        // Then
        verify(instructors).findById(instructorId);
        verify(instructors).delete(instructorId);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenInstructorDoesNotExist() {
        // Given
        UUID instructorId = UUID.randomUUID();

        when(instructors.findById(instructorId)).thenReturn(Optional.empty());

        // When & Then
        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                instructorDeleter.delete(instructorId)
        );

        assertEquals("Instructor does not exist", exception.getMessage());
        verify(instructors).findById(instructorId);
        verify(instructors, never()).delete(any());
    }
}
