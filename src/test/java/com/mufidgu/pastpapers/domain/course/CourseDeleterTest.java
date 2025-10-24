package com.mufidgu.pastpapers.domain.course;

import com.mufidgu.pastpapers.domain.common.exception.NotFoundException;
import com.mufidgu.pastpapers.domain.course.spi.Courses;
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
class CourseDeleterTest {

    @Mock
    private Courses courses;

    @InjectMocks
    private CourseDeleter courseDeleter;

    @Test
    void shouldDeleteCourseSuccessfully() {
        // Given
        UUID courseId = UUID.randomUUID();
        Course course = new Course(courseId, "AI", "Artificial Intelligence", List.of(), List.of());

        when(courses.findById(courseId)).thenReturn(Optional.of(course));
        doNothing().when(courses).delete(courseId);

        // When
        courseDeleter.delete(courseId);

        // Then
        verify(courses).findById(courseId);
        verify(courses).delete(courseId);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenCourseDoesNotExist() {
        // Given
        UUID courseId = UUID.randomUUID();

        when(courses.findById(courseId)).thenReturn(Optional.empty());

        // When & Then
        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                courseDeleter.delete(courseId)
        );

        assertEquals("Course does not exist", exception.getMessage());
        verify(courses).findById(courseId);
        verify(courses, never()).delete(any());
    }
}
