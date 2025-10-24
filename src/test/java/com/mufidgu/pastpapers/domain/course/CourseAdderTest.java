package com.mufidgu.pastpapers.domain.course;

import com.mufidgu.pastpapers.domain.common.exception.ConflictException;
import com.mufidgu.pastpapers.domain.common.exception.NotFoundException;
import com.mufidgu.pastpapers.domain.course.spi.Courses;
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
class CourseAdderTest {

    @Mock
    private Courses courses;

    @Mock
    private Degrees degrees;

    @Mock
    private Institutions institutions;

    @InjectMocks
    private CourseAdder courseAdder;

    private UUID degreeId;
    private UUID institutionId;

    @BeforeEach
    void setUp() {
        degreeId = UUID.randomUUID();
        institutionId = UUID.randomUUID();
    }

    @Test
    void shouldAddCourseSuccessfully() {
        // Given
        String shortName = "AI";
        String fullName = "Artificial Intelligence";
        List<UUID> degreeIds = List.of(degreeId);
        List<UUID> institutionIds = List.of(institutionId);

        when(courses.findByShortNameAndFullName(shortName, fullName)).thenReturn(Optional.empty());
        when(degrees.findById(degreeId)).thenReturn(Optional.of(mock(com.mufidgu.pastpapers.domain.degree.Degree.class)));
        when(institutions.findById(institutionId)).thenReturn(Optional.of(mock(com.mufidgu.pastpapers.domain.institution.Institution.class)));
        when(courses.save(any(Course.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Course result = courseAdder.add(shortName, fullName, degreeIds, institutionIds);

        // Then
        assertNotNull(result);
        assertEquals(shortName, result.shortName());
        assertEquals(fullName, result.fullName());
        assertEquals(degreeIds, result.degreeIds());
        assertEquals(institutionIds, result.institutionIds());
        assertNotNull(result.id());

        verify(courses).findByShortNameAndFullName(shortName, fullName);
        verify(degrees).findById(degreeId);
        verify(institutions).findById(institutionId);
        verify(courses).save(any(Course.class));
    }

    @Test
    void shouldThrowConflictExceptionWhenCourseAlreadyExists() {
        // Given
        String shortName = "AI";
        String fullName = "Artificial Intelligence";
        List<UUID> degreeIds = List.of(degreeId);
        List<UUID> institutionIds = List.of(institutionId);

        Course existingCourse = new Course(shortName, fullName, degreeIds, institutionIds);
        when(courses.findByShortNameAndFullName(shortName, fullName)).thenReturn(Optional.of(existingCourse));

        // When & Then
        ConflictException exception = assertThrows(ConflictException.class, () ->
                courseAdder.add(shortName, fullName, degreeIds, institutionIds)
        );

        assertEquals("Course with same short name and full name already exists", exception.getMessage());
        verify(courses).findByShortNameAndFullName(shortName, fullName);
        verify(degrees, never()).findById(any());
        verify(institutions, never()).findById(any());
        verify(courses, never()).save(any());
    }

    @Test
    void shouldThrowNotFoundExceptionWhenDegreeDoesNotExist() {
        // Given
        String shortName = "AI";
        String fullName = "Artificial Intelligence";
        List<UUID> degreeIds = List.of(degreeId);
        List<UUID> institutionIds = List.of(institutionId);

        when(courses.findByShortNameAndFullName(shortName, fullName)).thenReturn(Optional.empty());
        when(degrees.findById(degreeId)).thenReturn(Optional.empty());

        // When & Then
        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                courseAdder.add(shortName, fullName, degreeIds, institutionIds)
        );

        assertTrue(exception.getMessage().contains("Degree with ID"));
        assertTrue(exception.getMessage().contains("does not exist"));
        verify(courses).findByShortNameAndFullName(shortName, fullName);
        verify(degrees).findById(degreeId);
        verify(institutions, never()).findById(any());
        verify(courses, never()).save(any());
    }

    @Test
    void shouldThrowNotFoundExceptionWhenInstitutionDoesNotExist() {
        // Given
        String shortName = "AI";
        String fullName = "Artificial Intelligence";
        List<UUID> degreeIds = List.of(degreeId);
        List<UUID> institutionIds = List.of(institutionId);

        when(courses.findByShortNameAndFullName(shortName, fullName)).thenReturn(Optional.empty());
        when(degrees.findById(degreeId)).thenReturn(Optional.of(mock(com.mufidgu.pastpapers.domain.degree.Degree.class)));
        when(institutions.findById(institutionId)).thenReturn(Optional.empty());

        // When & Then
        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                courseAdder.add(shortName, fullName, degreeIds, institutionIds)
        );

        assertTrue(exception.getMessage().contains("Institution with ID"));
        assertTrue(exception.getMessage().contains("does not exist"));
        verify(courses).findByShortNameAndFullName(shortName, fullName);
        verify(degrees).findById(degreeId);
        verify(institutions).findById(institutionId);
        verify(courses, never()).save(any());
    }

    @Test
    void shouldAddCourseWithMultipleDegrees() {
        // Given
        String shortName = "ML";
        String fullName = "Machine Learning";
        UUID degreeId2 = UUID.randomUUID();
        List<UUID> degreeIds = List.of(degreeId, degreeId2);
        List<UUID> institutionIds = List.of(institutionId);

        when(courses.findByShortNameAndFullName(shortName, fullName)).thenReturn(Optional.empty());
        when(degrees.findById(degreeId)).thenReturn(Optional.of(mock(com.mufidgu.pastpapers.domain.degree.Degree.class)));
        when(degrees.findById(degreeId2)).thenReturn(Optional.of(mock(com.mufidgu.pastpapers.domain.degree.Degree.class)));
        when(institutions.findById(institutionId)).thenReturn(Optional.of(mock(com.mufidgu.pastpapers.domain.institution.Institution.class)));
        when(courses.save(any(Course.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Course result = courseAdder.add(shortName, fullName, degreeIds, institutionIds);

        // Then
        assertNotNull(result);
        assertEquals(2, result.degreeIds().size());
        verify(degrees).findById(degreeId);
        verify(degrees).findById(degreeId2);
    }

    @Test
    void shouldAddCourseWithMultipleInstitutions() {
        // Given
        String shortName = "DS";
        String fullName = "Data Science";
        UUID institutionId2 = UUID.randomUUID();
        List<UUID> degreeIds = List.of(degreeId);
        List<UUID> institutionIds = List.of(institutionId, institutionId2);

        when(courses.findByShortNameAndFullName(shortName, fullName)).thenReturn(Optional.empty());
        when(degrees.findById(degreeId)).thenReturn(Optional.of(mock(com.mufidgu.pastpapers.domain.degree.Degree.class)));
        when(institutions.findById(institutionId)).thenReturn(Optional.of(mock(com.mufidgu.pastpapers.domain.institution.Institution.class)));
        when(institutions.findById(institutionId2)).thenReturn(Optional.of(mock(com.mufidgu.pastpapers.domain.institution.Institution.class)));
        when(courses.save(any(Course.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Course result = courseAdder.add(shortName, fullName, degreeIds, institutionIds);

        // Then
        assertNotNull(result);
        assertEquals(2, result.institutionIds().size());
        verify(institutions).findById(institutionId);
        verify(institutions).findById(institutionId2);
    }
}
