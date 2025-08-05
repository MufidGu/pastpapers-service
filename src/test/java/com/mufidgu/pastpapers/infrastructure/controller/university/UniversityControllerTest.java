package com.mufidgu.pastpapers.infrastructure.controller.university;

import com.mufidgu.pastpapers.Application;
import com.mufidgu.pastpapers.domain.university.University;
import com.mufidgu.pastpapers.domain.university.spi.Universities;
import com.mufidgu.pastpapers.infrastructure.configuration.DomainConfiguration;
import ddd.Stub;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@Import(DomainConfiguration.class)
public class UniversityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Universities universities;

    @Test
    void should_add_university() throws Exception {
        mockMvc.perform(
                post("/university/add")
                        .contentType("application/json")
                        .content("""
                                {
                                    "shortName": "MIT",
                                    "fullName": "Massachusetts Institute of Technology"
                                }
                                """)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.shortName").value("MIT"))
                .andExpect(jsonPath("$.fullName").value("Massachusetts Institute of Technology"));
    }

    @Test
    void should_return_university() throws Exception {
        universities.save(new University("Stanford", "Stanford University"));

        mockMvc.perform(
                        get("/university/all")
                                .contentType("application/json")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[?(@.shortName == 'Stanford')]").exists());
    }

    @TestConfiguration
    @ComponentScan(
            basePackageClasses = {University.class},
            includeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, classes = {Stub.class})})
    static class StubConfiguration {
    }
}
