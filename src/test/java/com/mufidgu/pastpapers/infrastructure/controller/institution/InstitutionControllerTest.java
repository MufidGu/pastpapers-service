package com.mufidgu.pastpapers.infrastructure.controller.institution;

import com.mufidgu.pastpapers.domain.institution.Institution;
import com.mufidgu.pastpapers.domain.institution.spi.Institutions;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@Import(DomainConfiguration.class)
public class InstitutionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Institutions institutions;

    @Test
    void should_add_institution() throws Exception {
        mockMvc.perform(
                        post("/institution/add")
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
    void should_return_institution() throws Exception {
        institutions.save(new Institution("Stanford", "Stanford Institution"));

        mockMvc.perform(
                        get("/institution/all")
                                .contentType("application/json")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[?(@.shortName == 'Stanford')]").exists());
    }

    @Test
    void should_update_institution() throws Exception {
        Institution institution = institutions.save(new Institution("Harvard", "Harvard Institution"));

        mockMvc.perform(
                        put("/institution/update?institutionId=" + institution.id())
                                .contentType("application/json")
                                .content("""
                                        {
                                            "shortName": "Harvard Updated",
                                            "fullName": "Harvard Institution Updated"
                                        }
                                        """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(institution.id().toString()))
                .andExpect(jsonPath("$.shortName").value("Harvard Updated"))
                .andExpect(jsonPath("$.fullName").value("Harvard Institution Updated"));
    }


    @Test
    void should_delete_institution() throws Exception {
        Institution institution = institutions.save(new Institution("Yale", "Yale Institution"));

        mockMvc.perform(
                        delete("/institution/delete?institutionId=" + institution.id())
                )
                .andExpect(status().isOk());

        // Verify that the institution is deleted
        mockMvc.perform(get("/institution/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.shortName == 'Yale')]").doesNotExist());
    }

    @TestConfiguration
    @ComponentScan(
            basePackageClasses = {Institution.class},
            includeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, classes = {Stub.class})})
    static class StubConfiguration {
    }
}
