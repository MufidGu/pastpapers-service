package com.mufidgu.pastpapers.infrastructure.controller.instructor;

import com.mufidgu.pastpapers.domain.instructor.Instructor;
import com.mufidgu.pastpapers.domain.instructor.spi.Instructors;
import com.mufidgu.pastpapers.domain.course.Course;
import com.mufidgu.pastpapers.domain.course.spi.Courses;
import com.mufidgu.pastpapers.domain.institution.Institution;
import com.mufidgu.pastpapers.domain.institution.spi.Institutions;
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
    private Institutions institutions;

    private Course testCourse;
    private Institution testInstitution;

    @BeforeEach
    void setUp() {
        // Create test course and institution to use in instructor tests
        testCourse = courses.save(new Course("AI", "Artificial Intelligence", List.of(), List.of()));
        testInstitution = institutions.save(new Institution("MIT", "Massachusetts Institute of Technology"));
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
                                    "institutionIds": ["%s"]
                                }
                                """, testCourse.id(), testInstitution.id()))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.fullName").value("John Doe"))
                .andExpect(jsonPath("$.courseIds[0]").value(testCourse.id().toString()))
                .andExpect(jsonPath("$.institutionIds[0]").value(testInstitution.id().toString()));
    }

    @Test
    void should_return_all_instructors() throws Exception {
        // Create a test instructor
        instructors.save(new Instructor("Jane Smith", List.of(testCourse.id()), List.of(testInstitution.id())));

        mockMvc.perform(
                get("/instructor/list")
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
        Instructor instructor = instructors.save(new Instructor("Robert Brown", List.of(testCourse.id()), List.of(testInstitution.id())));

        mockMvc.perform(
                put("/instructor/update?instructorId=" + instructor.id())
                        .contentType("application/json")
                        .content(String.format("""
                                {
                                    "fullName": "Robert Green",
                                    "courseIds": ["%s"],
                                    "institutionIds": ["%s"]
                                }
                                """, testCourse.id(), testInstitution.id()))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(instructor.id().toString()))
                .andExpect(jsonPath("$.fullName").value("Robert Green"))
                .andExpect(jsonPath("$.courseIds[0]").value(testCourse.id().toString()))
                .andExpect(jsonPath("$.institutionIds[0]").value(testInstitution.id().toString()));
    }

    @Test
    void should_delete_instructor() throws Exception {
        // Create a test instructor
        Instructor instructor = instructors.save(new Instructor("David Wilson", List.of(testCourse.id()), List.of(testInstitution.id())));

        mockMvc.perform(
                delete("/instructor/delete?instructorId=" + instructor.id())
        )
                .andExpect(status().isOk());

        // Verify that the instructor is deleted
        mockMvc.perform(get("/instructor/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.fullName == 'David Wilson')]").doesNotExist());
    }

    @TestConfiguration
    @ComponentScan(
            basePackageClasses = {Instructor.class, Course.class, Institution.class},
            includeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, classes = {Stub.class})})
    static class StubConfiguration {
    }
}
