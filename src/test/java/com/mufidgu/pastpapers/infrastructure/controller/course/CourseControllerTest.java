package com.mufidgu.pastpapers.infrastructure.controller.course;

import com.mufidgu.pastpapers.domain.course.Course;
import com.mufidgu.pastpapers.domain.course.spi.Courses;
import com.mufidgu.pastpapers.domain.degree.Degree;
import com.mufidgu.pastpapers.domain.degree.spi.Degrees;
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
public class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Courses courses;

    @Autowired
    private Degrees degrees;

    @Autowired
    private Universities universities;

    private Degree testDegree;
    private University testUniversity;

    @BeforeEach
    void setUp() {
        // Create test degree and university to use in course tests
        testDegree = degrees.save(new Degree("CS", "Computer Science", List.of()));
        testUniversity = universities.save(new University("MIT", "Massachusetts Institute of Technology"));
    }

    @Test
    void should_add_course() throws Exception {
        mockMvc.perform(
                        post("/course/add")
                                .contentType("application/json")
                                .content(String.format("""
                                        {
                                            "shortName": "AI",
                                            "fullName": "Artificial Intelligence",
                                            "degreeIds": ["%s"],
                                            "universityIds": ["%s"]
                                        }
                                        """, testDegree.id(), testUniversity.id()))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.shortName").value("AI"))
                .andExpect(jsonPath("$.fullName").value("Artificial Intelligence"))
                .andExpect(jsonPath("$.degreeIds[0]").value(testDegree.id().toString()))
                .andExpect(jsonPath("$.universityIds[0]").value(testUniversity.id().toString()));
    }

    @Test
    void should_return_all_courses() throws Exception {
        // Create a test course
        courses.save(new Course("ML", "Machine Learning", List.of(testDegree.id()), List.of(testUniversity.id())));

        mockMvc.perform(
                        get("/course/all")
                                .contentType("application/json")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[?(@.shortName == 'ML')]").exists());
    }

    @Test
    void should_update_course() throws Exception {
        // Create a test course
        Course course = courses.save(new Course("DS", "Data Science", List.of(testDegree.id()), List.of(testUniversity.id())));

        mockMvc.perform(
                        put("/course/update?courseId=" + course.id())
                                .contentType("application/json")
                                .content(String.format("""
                                        {
                                            "shortName": "DataSci",
                                            "fullName": "Data Science and Analytics",
                                            "degreeIds": ["%s"],
                                            "universityIds": ["%s"]
                                        }
                                        """, testDegree.id(), testUniversity.id()))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(course.id().toString()))
                .andExpect(jsonPath("$.shortName").value("DataSci"))
                .andExpect(jsonPath("$.fullName").value("Data Science and Analytics"))
                .andExpect(jsonPath("$.degreeIds[0]").value(testDegree.id().toString()))
                .andExpect(jsonPath("$.universityIds[0]").value(testUniversity.id().toString()));
    }

    @Test
    void should_delete_course() throws Exception {
        // Create a test course
        Course course = courses.save(new Course("CyberSec", "Cyber Security", List.of(testDegree.id()), List.of(testUniversity.id())));

        mockMvc.perform(
                        delete("/course/delete?courseId=" + course.id())
                )
                .andExpect(status().isOk());

        // Verify that the course is deleted
        mockMvc.perform(get("/course/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.shortName == 'CyberSec')]").doesNotExist());
    }

    @TestConfiguration
    @ComponentScan(
            basePackageClasses = {Course.class, Degree.class, University.class},
            includeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, classes = {Stub.class})})
    static class StubConfiguration {
    }
}

