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
class InstructorAdderTest {

    @Mock
    private Instructors instructors;

    @Mock
    private Courses courses;

    @Mock
    private Institutions institutions;

    @InjectMocks
    private InstructorAdder instructorAdder;

    private UUID courseId;
    private UUID institutionId;

    @BeforeEach
    void setUp() {
        courseId = UUID.randomUUID();
        institutionId = UUID.randomUUID();
    }

    @Test
    void shouldAddInstructorSuccessfully() {
        // Given
        String fullName = "Dr. John Smith";
        List<UUID> courseIds = List.of(courseId);
        List<UUID> institutionIds = List.of(institutionId);

        when(instructors.findByFullName(fullName)).thenReturn(Optional.empty());
        when(courses.findById(courseId)).thenReturn(Optional.of(mock(Course.class)));
        when(institutions.findById(institutionId)).thenReturn(Optional.of(mock(Institution.class)));
        when(instructors.save(any(Instructor.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Instructor result = instructorAdder.add(fullName, courseIds, institutionIds);

        // Then
        assertNotNull(result);
        assertEquals(fullName, result.fullName());
        assertEquals(courseIds, result.courseIds());
        assertEquals(institutionIds, result.institutionIds());
        assertNotNull(result.id());

        verify(instructors).findByFullName(fullName);
        verify(courses).findById(courseId);
        verify(institutions).findById(institutionId);
        verify(instructors).save(any(Instructor.class));
    }

    @Test
    void shouldThrowConflictExceptionWhenInstructorAlreadyExists() {
        // Given
        String fullName = "Dr. John Smith";
        List<UUID> courseIds = List.of(courseId);
        List<UUID> institutionIds = List.of(institutionId);

        Instructor existingInstructor = new Instructor(fullName, courseIds, institutionIds);
        when(instructors.findByFullName(fullName)).thenReturn(Optional.of(existingInstructor));

        // When & Then
        ConflictException exception = assertThrows(ConflictException.class, () ->
                instructorAdder.add(fullName, courseIds, institutionIds)
        );

        assertEquals("Instructor with same full name already exists", exception.getMessage());
        verify(instructors).findByFullName(fullName);
        verify(courses, never()).findById(any());
        verify(institutions, never()).findById(any());
        verify(instructors, never()).save(any());
    }

    @Test
    void shouldThrowNotFoundExceptionWhenCourseDoesNotExist() {
        // Given
        String fullName = "Dr. John Smith";
        List<UUID> courseIds = List.of(courseId);
        List<UUID> institutionIds = List.of(institutionId);

        when(instructors.findByFullName(fullName)).thenReturn(Optional.empty());
        when(courses.findById(courseId)).thenReturn(Optional.empty());

        // When & Then
        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                instructorAdder.add(fullName, courseIds, institutionIds)
        );

        assertTrue(exception.getMessage().contains("Course with ID"));
        assertTrue(exception.getMessage().contains("does not exist"));
        verify(instructors).findByFullName(fullName);
        verify(courses).findById(courseId);
        verify(institutions, never()).findById(any());
        verify(instructors, never()).save(any());
    }

    @Test
    void shouldThrowNotFoundExceptionWhenInstitutionDoesNotExist() {
        // Given
        String fullName = "Dr. John Smith";
        List<UUID> courseIds = List.of(courseId);
        List<UUID> institutionIds = List.of(institutionId);

        when(instructors.findByFullName(fullName)).thenReturn(Optional.empty());
        when(courses.findById(courseId)).thenReturn(Optional.of(mock(Course.class)));
        when(institutions.findById(institutionId)).thenReturn(Optional.empty());

        // When & Then
        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                instructorAdder.add(fullName, courseIds, institutionIds)
        );

        assertTrue(exception.getMessage().contains("Institution with ID"));
        assertTrue(exception.getMessage().contains("does not exist"));
        verify(instructors).findByFullName(fullName);
        verify(courses).findById(courseId);
        verify(institutions).findById(institutionId);
        verify(instructors, never()).save(any());
    }

    @Test
    void shouldAddInstructorWithMultipleCoursesAndInstitutions() {
        // Given
        String fullName = "Dr. Jane Doe";
        UUID courseId2 = UUID.randomUUID();
        UUID institutionId2 = UUID.randomUUID();
        List<UUID> courseIds = List.of(courseId, courseId2);
        List<UUID> institutionIds = List.of(institutionId, institutionId2);

        when(instructors.findByFullName(fullName)).thenReturn(Optional.empty());
        when(courses.findById(courseId)).thenReturn(Optional.of(mock(Course.class)));
        when(courses.findById(courseId2)).thenReturn(Optional.of(mock(Course.class)));
        when(institutions.findById(institutionId)).thenReturn(Optional.of(mock(Institution.class)));
        when(institutions.findById(institutionId2)).thenReturn(Optional.of(mock(Institution.class)));
        when(instructors.save(any(Instructor.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Instructor result = instructorAdder.add(fullName, courseIds, institutionIds);

        // Then
        assertNotNull(result);
        assertEquals(2, result.courseIds().size());
        assertEquals(2, result.institutionIds().size());
        verify(courses).findById(courseId);
        verify(courses).findById(courseId2);
        verify(institutions).findById(institutionId);
        verify(institutions).findById(institutionId2);
    }
}
