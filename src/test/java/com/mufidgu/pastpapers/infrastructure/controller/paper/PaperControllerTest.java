package com.mufidgu.pastpapers.infrastructure.controller.paper;

import com.mufidgu.pastpapers.domain.course.Course;
import com.mufidgu.pastpapers.domain.course.spi.Courses;
import com.mufidgu.pastpapers.domain.degree.Degree;
import com.mufidgu.pastpapers.domain.degree.spi.Degrees;
import com.mufidgu.pastpapers.domain.instructor.Instructor;
import com.mufidgu.pastpapers.domain.instructor.spi.Instructors;
import com.mufidgu.pastpapers.domain.paper.Paper;
import com.mufidgu.pastpapers.domain.paper.enums.Season;
import com.mufidgu.pastpapers.domain.paper.enums.Shift;
import com.mufidgu.pastpapers.domain.paper.enums.Type;
import com.mufidgu.pastpapers.domain.paper.spi.FileStorage;
import com.mufidgu.pastpapers.domain.paper.spi.Papers;
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
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.matchesPattern;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
@Import(DomainConfiguration.class)
public class PaperControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Papers papers;

    @Autowired
    private FileStorage fileStorage;

    @Autowired
    private Courses courses;

    @Autowired
    private Instructors instructors;

    @Autowired
    private Universities universities;

    @Autowired
    private Degrees degrees;

    private Course testCourse;
    private Instructor testInstructor;
    private University testUniversity;
    private Degree testDegree;
    private Paper testPaper;

    @BeforeEach
    void setUp() {
        // Create test entities to use in paper tests
        testCourse = courses.save(new Course("AI", "Artificial Intelligence", List.of(), List.of()));
        testInstructor = instructors.save(new Instructor("John Smith", List.of(), List.of()));
        testUniversity = universities.save(new University("MIT", "Massachusetts Institute of Technology"));
        testDegree = degrees.save(new Degree("CS", "Computer Science", List.of()));

        UUID id = UUID.randomUUID();
        testPaper = papers.save(new Paper(
                id,
                testInstructor.id(),
                testCourse.id(),
                Type.FINAL,
                testUniversity.id(),
                testDegree.id(),
                Shift.MORNING,
                5,
                'A',
                LocalDate.now().getYear(),
                Season.SPRING,
                Date.valueOf(LocalDate.of(2023, 5, 15)),
                "test.pdf"
        ));


        fileStorage.store(
                "PDF content".getBytes(),
                testPaper.id().toString()
        );
    }

    @Test
    void should_upload_paper() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test-paper.pdf",
                MediaType.APPLICATION_PDF_VALUE,
                "PDF content".getBytes()
        );

        mockMvc.perform(
                multipart("/paper/upload")
                        .file(file)
        )
                .andExpect(status().isOk())
                .andExpect(content().string(matchesPattern("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}")));
    }

    @Test
    void should_download_paper() throws Exception {
        // Setup a test file in the papers repository
        // This would typically be done by the domain service in a real test

        mockMvc.perform(
                get("/paper/download")
                        .param("paperId", testPaper.id().toString())
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk());
    }

    @Test
    void should_update_paper() throws Exception {
        mockMvc.perform(
                put("/paper/update")
                        .param("paperId", testPaper.id().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                {
                                    "instructorId": "%s",
                                    "courseId": "%s",
                                    "type": "MIDTERM",
                                    "universityId": "%s",
                                    "degreeId": "%s",
                                    "shift": "EVENING",
                                    "semester": 6,
                                    "section": "B",
                                    "year": 2024,
                                    "season": "FALL",
                                    "date": "2024-10-15"
                                }
                                """, testInstructor.id(), testCourse.id(), testUniversity.id(), testDegree.id()))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testPaper.id().toString()))
                .andExpect(jsonPath("$.instructorId").value(testInstructor.id().toString()))
                .andExpect(jsonPath("$.courseId").value(testCourse.id().toString()))
                .andExpect(jsonPath("$.type").value("MIDTERM"))
                .andExpect(jsonPath("$.universityId").value(testUniversity.id().toString()))
                .andExpect(jsonPath("$.degreeId").value(testDegree.id().toString()))
                .andExpect(jsonPath("$.shift").value("EVENING"))
                .andExpect(jsonPath("$.semester").value(6))
                .andExpect(jsonPath("$.section").value("B"))
                .andExpect(jsonPath("$.year").value(2024))
                .andExpect(jsonPath("$.season").value("FALL"));
    }

    @Test
    void should_delete_paper() throws Exception {
        mockMvc.perform(
                delete("/paper/delete")
                        .param("paperId", testPaper.id().toString())
        )
                .andExpect(status().isOk())
                .andExpect(content().string("Paper deleted successfully"));
    }

    @Test
    void should_list_all_papers() throws Exception {
        mockMvc.perform(
                get("/paper/list")
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[*].id").value(hasItem(testPaper.id().toString())));
    }

    @TestConfiguration
    @ComponentScan(
            basePackageClasses = {Paper.class, Course.class, Instructor.class, University.class, Degree.class},
            includeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, classes = {Stub.class})})
    static class StubConfiguration {
    }
}
