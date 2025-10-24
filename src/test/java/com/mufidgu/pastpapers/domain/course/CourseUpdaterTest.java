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
class CourseUpdaterTest {

    @Mock
    private Courses courses;

    @Mock
    private Degrees degrees;

    @Mock
    private Institutions institutions;

    @InjectMocks
    private CourseUpdater courseUpdater;

    private UUID courseId;
    private UUID degreeId;
    private UUID institutionId;

    @BeforeEach
    void setUp() {
        courseId = UUID.randomUUID();
        degreeId = UUID.randomUUID();
        institutionId = UUID.randomUUID();
    }

    @Test
    void shouldUpdateCourseSuccessfully() {
        // Given
        String newShortName = "AI-Adv";
        String newFullName = "Advanced Artificial Intelligence";
        List<UUID> newDegreeIds = List.of(degreeId);
        List<UUID> newInstitutionIds = List.of(institutionId);

        Course existingCourse = new Course(courseId, "AI", "Artificial Intelligence", List.of(), List.of());

        when(courses.findById(courseId)).thenReturn(Optional.of(existingCourse));
        when(courses.findByShortNameAndFullName(newShortName, newFullName)).thenReturn(Optional.empty());
        when(degrees.findById(degreeId)).thenReturn(Optional.of(mock(com.mufidgu.pastpapers.domain.degree.Degree.class)));
        when(institutions.findById(institutionId)).thenReturn(Optional.of(mock(com.mufidgu.pastpapers.domain.institution.Institution.class)));
        when(courses.save(any(Course.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Course result = courseUpdater.update(courseId, newShortName, newFullName, newDegreeIds, newInstitutionIds);

        // Then
        assertNotNull(result);
        assertEquals(courseId, result.id());
        assertEquals(newShortName, result.shortName());
        assertEquals(newFullName, result.fullName());
        assertEquals(newDegreeIds, result.degreeIds());
        assertEquals(newInstitutionIds, result.institutionIds());

        verify(courses).findById(courseId);
        verify(courses).findByShortNameAndFullName(newShortName, newFullName);
        verify(degrees).findById(degreeId);
        verify(institutions).findById(institutionId);
        verify(courses).save(any(Course.class));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenCourseDoesNotExist() {
        // Given
        String newShortName = "AI-Adv";
        String newFullName = "Advanced Artificial Intelligence";
        List<UUID> newDegreeIds = List.of(degreeId);
        List<UUID> newInstitutionIds = List.of(institutionId);

        when(courses.findById(courseId)).thenReturn(Optional.empty());

        // When & Then
        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                courseUpdater.update(courseId, newShortName, newFullName, newDegreeIds, newInstitutionIds)
        );

        assertEquals("Course does not exist", exception.getMessage());
        verify(courses).findById(courseId);
        verify(courses, never()).findByShortNameAndFullName(any(), any());
        verify(degrees, never()).findById(any());
        verify(institutions, never()).findById(any());
        verify(courses, never()).save(any());
    }

    @Test
    void shouldThrowConflictExceptionWhenUpdatedNameExistsForDifferentCourse() {
        // Given
        String newShortName = "ML";
        String newFullName = "Machine Learning";
        List<UUID> newDegreeIds = List.of(degreeId);
        List<UUID> newInstitutionIds = List.of(institutionId);

        Course existingCourse = new Course(courseId, "AI", "Artificial Intelligence", List.of(), List.of());
        UUID otherCourseId = UUID.randomUUID();
        Course conflictingCourse = new Course(otherCourseId, newShortName, newFullName, List.of(), List.of());

        when(courses.findById(courseId)).thenReturn(Optional.of(existingCourse));
        when(courses.findByShortNameAndFullName(newShortName, newFullName)).thenReturn(Optional.of(conflictingCourse));

        // When & Then
        ConflictException exception = assertThrows(ConflictException.class, () ->
                courseUpdater.update(courseId, newShortName, newFullName, newDegreeIds, newInstitutionIds)
        );

        assertEquals("Course with same short name and full name already exists", exception.getMessage());
        verify(courses).findById(courseId);
        verify(courses).findByShortNameAndFullName(newShortName, newFullName);
        verify(degrees, never()).findById(any());
        verify(institutions, never()).findById(any());
        verify(courses, never()).save(any());
    }

    @Test
    void shouldAllowUpdateWithSameNameForSameCourse() {
        // Given
        String sameName = "AI";
        String sameFullName = "Artificial Intelligence";
        List<UUID> newDegreeIds = List.of(degreeId);
        List<UUID> newInstitutionIds = List.of(institutionId);

        Course existingCourse = new Course(courseId, sameName, sameFullName, List.of(), List.of());

        when(courses.findById(courseId)).thenReturn(Optional.of(existingCourse));
        when(courses.findByShortNameAndFullName(sameName, sameFullName)).thenReturn(Optional.of(existingCourse));
        when(degrees.findById(degreeId)).thenReturn(Optional.of(mock(com.mufidgu.pastpapers.domain.degree.Degree.class)));
        when(institutions.findById(institutionId)).thenReturn(Optional.of(mock(com.mufidgu.pastpapers.domain.institution.Institution.class)));
        when(courses.save(any(Course.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Course result = courseUpdater.update(courseId, sameName, sameFullName, newDegreeIds, newInstitutionIds);

        // Then
        assertNotNull(result);
        assertEquals(courseId, result.id());
        verify(courses).save(any(Course.class));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenDegreeDoesNotExist() {
        // Given
        String newShortName = "AI-Adv";
        String newFullName = "Advanced Artificial Intelligence";
        List<UUID> newDegreeIds = List.of(degreeId);
        List<UUID> newInstitutionIds = List.of(institutionId);

        Course existingCourse = new Course(courseId, "AI", "Artificial Intelligence", List.of(), List.of());

        when(courses.findById(courseId)).thenReturn(Optional.of(existingCourse));
        when(courses.findByShortNameAndFullName(newShortName, newFullName)).thenReturn(Optional.empty());
        when(degrees.findById(degreeId)).thenReturn(Optional.empty());

        // When & Then
        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                courseUpdater.update(courseId, newShortName, newFullName, newDegreeIds, newInstitutionIds)
        );

        assertTrue(exception.getMessage().contains("Degree with ID"));
        assertTrue(exception.getMessage().contains("does not exist"));
        verify(courses).findById(courseId);
        verify(degrees).findById(degreeId);
        verify(institutions, never()).findById(any());
        verify(courses, never()).save(any());
    }

    @Test
    void shouldThrowNotFoundExceptionWhenInstitutionDoesNotExist() {
        // Given
        String newShortName = "AI-Adv";
        String newFullName = "Advanced Artificial Intelligence";
        List<UUID> newDegreeIds = List.of(degreeId);
        List<UUID> newInstitutionIds = List.of(institutionId);

        Course existingCourse = new Course(courseId, "AI", "Artificial Intelligence", List.of(), List.of());

        when(courses.findById(courseId)).thenReturn(Optional.of(existingCourse));
        when(courses.findByShortNameAndFullName(newShortName, newFullName)).thenReturn(Optional.empty());
        when(degrees.findById(degreeId)).thenReturn(Optional.of(mock(com.mufidgu.pastpapers.domain.degree.Degree.class)));
        when(institutions.findById(institutionId)).thenReturn(Optional.empty());

        // When & Then
        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                courseUpdater.update(courseId, newShortName, newFullName, newDegreeIds, newInstitutionIds)
        );

        assertTrue(exception.getMessage().contains("Institution with ID"));
        assertTrue(exception.getMessage().contains("does not exist"));
        verify(courses).findById(courseId);
        verify(degrees).findById(degreeId);
        verify(institutions).findById(institutionId);
        verify(courses, never()).save(any());
    }
}
