package com.mufidgu.pastpapers.domain.course;

import com.mufidgu.pastpapers.domain.course.spi.Courses;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseListerTest {

    @Mock
    private Courses courses;

    @InjectMocks
    private CourseLister courseLister;

    @Test
    void shouldReturnAllCourses() {
        // Given
        Course course1 = new Course("AI", "Artificial Intelligence", List.of(), List.of());
        Course course2 = new Course("ML", "Machine Learning", List.of(), List.of());
        List<Course> expectedCourses = List.of(course1, course2);

        when(courses.findAll()).thenReturn(expectedCourses);

        // When
        List<Course> result = courseLister.listAll();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedCourses, result);
        verify(courses).findAll();
    }

    @Test
    void shouldReturnEmptyListWhenNoCourses() {
        // Given
        when(courses.findAll()).thenReturn(Collections.emptyList());

        // When
        List<Course> result = courseLister.listAll();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(courses).findAll();
    }
}
