package com.mufidgu.pastpapers.infrastructure.controller.instructor;

import com.mufidgu.pastpapers.domain.instructor.Instructor;
import com.mufidgu.pastpapers.domain.instructor.spi.Instructors;
import com.mufidgu.pastpapers.domain.course.Course;
import com.mufidgu.pastpapers.domain.course.spi.Courses;
import com.mufidgu.pastpapers.domain.university.University;
import com.mufidgu.pastpapers.domain.university.spi.Universities;
import com.mufidgu.pastpapers.infrastructure.configuration.DomainConfiguration;
import ddd.Stub;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@Import(DomainConfiguration.class)
public class InstructorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Instructors instructors;

    @Autowired
    private Courses courses;

    @Autowired
    private Universities universities;

    private Course testCourse;
    private University testUniversity;

    @BeforeEach
    void setUp() {
        // Create test course and university to use in instructor tests
        testCourse = courses.save(new Course("AI", "Artificial Intelligence", List.of(), List.of()));
        testUniversity = universities.save(new University("MIT", "Massachusetts Institute of Technology"));
    }

    @Test
    void should_add_instructor() throws Exception {
        mockMvc.perform(
                post("/instructor/add")
                        .contentType("application/json")
                        .content(String.format("""
                                {
                                    "fullName": "John Doe",
                                    "courseIds": ["%s"],
                                    "universityIds": ["%s"]
                                }
                                """, testCourse.id(), testUniversity.id()))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.fullName").value("John Doe"))
                .andExpect(jsonPath("$.courseIds[0]").value(testCourse.id().toString()))
                .andExpect(jsonPath("$.universityIds[0]").value(testUniversity.id().toString()));
    }

    @Test
    void should_return_all_instructors() throws Exception {
        // Create a test instructor
        instructors.save(new Instructor("Jane Smith", List.of(testCourse.id()), List.of(testUniversity.id())));

        mockMvc.perform(
                get("/instructor/all")
                        .contentType("application/json")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[?(@.fullName == 'Jane Smith')]").exists());
    }

    @Test
    void should_update_instructor() throws Exception {
        // Create a test instructor
        Instructor instructor = instructors.save(new Instructor("Robert Brown", List.of(testCourse.id()), List.of(testUniversity.id())));

        mockMvc.perform(
                put("/instructor/update?instructorId=" + instructor.id())
                        .contentType("application/json")
                        .content(String.format("""
                                {
                                    "fullName": "Robert Green",
                                    "courseIds": ["%s"],
                                    "universityIds": ["%s"]
                                }
                                """, testCourse.id(), testUniversity.id()))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(instructor.id().toString()))
                .andExpect(jsonPath("$.fullName").value("Robert Green"))
                .andExpect(jsonPath("$.courseIds[0]").value(testCourse.id().toString()))
                .andExpect(jsonPath("$.universityIds[0]").value(testUniversity.id().toString()));
    }

    @Test
    void should_delete_instructor() throws Exception {
        // Create a test instructor
        Instructor instructor = instructors.save(new Instructor("David Wilson", List.of(testCourse.id()), List.of(testUniversity.id())));

        mockMvc.perform(
                delete("/instructor/delete?instructorId=" + instructor.id())
        )
                .andExpect(status().isOk());

        // Verify that the instructor is deleted
        mockMvc.perform(get("/instructor/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.fullName == 'David Wilson')]").doesNotExist());
    }

    @TestConfiguration
    @ComponentScan(
            basePackageClasses = {Instructor.class, Course.class, University.class},
            includeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, classes = {Stub.class})})
    static class StubConfiguration {
    }
}
