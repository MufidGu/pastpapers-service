package com.mufidgu.pastpapers.domain.instructor;

import com.mufidgu.pastpapers.domain.common.exception.ConflictException;
import com.mufidgu.pastpapers.domain.common.exception.NotFoundException;
import com.mufidgu.pastpapers.domain.course.Course;
import com.mufidgu.pastpapers.domain.course.spi.Courses;
import com.mufidgu.pastpapers.domain.institution.Institution;
import com.mufidgu.pastpapers.domain.institution.spi.Institutions;
import com.mufidgu.pastpapers.domain.instructor.spi.Instructors;
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
class InstructorUpdaterTest {

    @Mock
    private Instructors instructors;

    @Mock
    private Courses courses;

    @Mock
    private Institutions institutions;

    @InjectMocks
    private InstructorUpdater instructorUpdater;

    private UUID instructorId;
    private UUID courseId;
    private UUID institutionId;

    @BeforeEach
    void setUp() {
        instructorId = UUID.randomUUID();
        courseId = UUID.randomUUID();
        institutionId = UUID.randomUUID();
    }

    @Test
    void shouldUpdateInstructorSuccessfully() {
        // Given
        String newFullName = "Dr. John Smith Jr.";
        List<UUID> newCourseIds = List.of(courseId);
        List<UUID> newInstitutionIds = List.of(institutionId);

        Instructor existingInstructor = new Instructor(instructorId, "Dr. John Smith", List.of(), List.of());

        when(instructors.findById(instructorId)).thenReturn(Optional.of(existingInstructor));
        when(instructors.findByFullName(newFullName)).thenReturn(Optional.empty());
        when(courses.findById(courseId)).thenReturn(Optional.of(mock(Course.class)));
        when(institutions.findById(institutionId)).thenReturn(Optional.of(mock(Institution.class)));
        when(instructors.save(any(Instructor.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Instructor result = instructorUpdater.update(instructorId, newFullName, newCourseIds, newInstitutionIds);

        // Then
        assertNotNull(result);
        assertEquals(instructorId, result.id());
        assertEquals(newFullName, result.fullName());
        assertEquals(newCourseIds, result.courseIds());
        assertEquals(newInstitutionIds, result.institutionIds());

        verify(instructors).findById(instructorId);
        verify(instructors).findByFullName(newFullName);
        verify(courses).findById(courseId);
        verify(institutions).findById(institutionId);
        verify(instructors).save(any(Instructor.class));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenInstructorDoesNotExist() {
        // Given
        String newFullName = "Dr. John Smith Jr.";
        List<UUID> newCourseIds = List.of(courseId);
        List<UUID> newInstitutionIds = List.of(institutionId);

        when(instructors.findById(instructorId)).thenReturn(Optional.empty());

        // When & Then
        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                instructorUpdater.update(instructorId, newFullName, newCourseIds, newInstitutionIds)
        );

        assertEquals("Instructor does not exist", exception.getMessage());
        verify(instructors).findById(instructorId);
        verify(instructors, never()).findByFullName(any());
        verify(courses, never()).findById(any());
        verify(institutions, never()).findById(any());
        verify(instructors, never()).save(any());
    }

    @Test
    void shouldThrowConflictExceptionWhenUpdatedNameExistsForDifferentInstructor() {
        // Given
        String newFullName = "Dr. Jane Doe";
        List<UUID> newCourseIds = List.of(courseId);
        List<UUID> newInstitutionIds = List.of(institutionId);

        Instructor existingInstructor = new Instructor(instructorId, "Dr. John Smith", List.of(), List.of());
        UUID otherInstructorId = UUID.randomUUID();
        Instructor conflictingInstructor = new Instructor(otherInstructorId, newFullName, List.of(), List.of());

        when(instructors.findById(instructorId)).thenReturn(Optional.of(existingInstructor));
        when(instructors.findByFullName(newFullName)).thenReturn(Optional.of(conflictingInstructor));

        // When & Then
        ConflictException exception = assertThrows(ConflictException.class, () ->
                instructorUpdater.update(instructorId, newFullName, newCourseIds, newInstitutionIds)
        );

        assertEquals("Instructor with same full name already exists", exception.getMessage());
        verify(instructors).findById(instructorId);
        verify(instructors).findByFullName(newFullName);
        verify(courses, never()).findById(any());
        verify(institutions, never()).findById(any());
        verify(instructors, never()).save(any());
    }

    @Test
    void shouldAllowUpdateWithSameNameForSameInstructor() {
        // Given
        String sameName = "Dr. John Smith";
        List<UUID> newCourseIds = List.of(courseId);
        List<UUID> newInstitutionIds = List.of(institutionId);

        Instructor existingInstructor = new Instructor(instructorId, sameName, List.of(), List.of());

        when(instructors.findById(instructorId)).thenReturn(Optional.of(existingInstructor));
        when(instructors.findByFullName(sameName)).thenReturn(Optional.of(existingInstructor));
        when(courses.findById(courseId)).thenReturn(Optional.of(mock(Course.class)));
        when(institutions.findById(institutionId)).thenReturn(Optional.of(mock(Institution.class)));
        when(instructors.save(any(Instructor.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Instructor result = instructorUpdater.update(instructorId, sameName, newCourseIds, newInstitutionIds);

        // Then
        assertNotNull(result);
        assertEquals(instructorId, result.id());
        verify(instructors).save(any(Instructor.class));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenCourseDoesNotExist() {
        // Given
        String newFullName = "Dr. John Smith Jr.";
        List<UUID> newCourseIds = List.of(courseId);
        List<UUID> newInstitutionIds = List.of(institutionId);

        Instructor existingInstructor = new Instructor(instructorId, "Dr. John Smith", List.of(), List.of());

        when(instructors.findById(instructorId)).thenReturn(Optional.of(existingInstructor));
        when(instructors.findByFullName(newFullName)).thenReturn(Optional.empty());
        when(courses.findById(courseId)).thenReturn(Optional.empty());

        // When & Then
        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                instructorUpdater.update(instructorId, newFullName, newCourseIds, newInstitutionIds)
        );

        assertTrue(exception.getMessage().contains("Course with ID"));
        assertTrue(exception.getMessage().contains("does not exist"));
        verify(instructors).findById(instructorId);
        verify(courses).findById(courseId);
        verify(institutions, never()).findById(any());
        verify(instructors, never()).save(any());
    }

    @Test
    void shouldThrowNotFoundExceptionWhenInstitutionDoesNotExist() {
        // Given
        String newFullName = "Dr. John Smith Jr.";
        List<UUID> newCourseIds = List.of(courseId);
        List<UUID> newInstitutionIds = List.of(institutionId);

        Instructor existingInstructor = new Instructor(instructorId, "Dr. John Smith", List.of(), List.of());

        when(instructors.findById(instructorId)).thenReturn(Optional.of(existingInstructor));
        when(instructors.findByFullName(newFullName)).thenReturn(Optional.empty());
        when(courses.findById(courseId)).thenReturn(Optional.of(mock(Course.class)));
        when(institutions.findById(institutionId)).thenReturn(Optional.empty());

        // When & Then
        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                instructorUpdater.update(instructorId, newFullName, newCourseIds, newInstitutionIds)
        );

        assertTrue(exception.getMessage().contains("Institution with ID"));
        assertTrue(exception.getMessage().contains("does not exist"));
        verify(instructors).findById(instructorId);
        verify(courses).findById(courseId);
        verify(institutions).findById(institutionId);
        verify(instructors, never()).save(any());
    }
}
