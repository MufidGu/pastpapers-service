package com.mufidgu.pastpapers.infrastructure.controller.degree;

import com.mufidgu.pastpapers.domain.degree.Degree;
import com.mufidgu.pastpapers.domain.degree.spi.Degrees;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@Import(DomainConfiguration.class)
public class DegreeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Degrees degrees;

    @Autowired
    private Institutions institutions;

    private Institution testInstitution;
    private Institution secondTestInstitution;

    @BeforeEach
    void setUp() {
        // Create test institutions to use in degree tests
        testInstitution = institutions.save(new Institution("MIT", "Massachusetts Institute of Technology"));
        secondTestInstitution = institutions.save(new Institution("Stanford", "Stanford Institution"));
    }

    @Test
    void should_add_degree() throws Exception {
        mockMvc.perform(
                        post("/degree/add")
                                .contentType("application/json")
                                .content(String.format("""
                                        {
                                            "shortName": "CS",
                                            "fullName": "Computer Science",
                                            "institutionIds": ["%s"]
                                        }
                                        """, testInstitution.id()))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.shortName").value("CS"))
                .andExpect(jsonPath("$.fullName").value("Computer Science"))
                .andExpect(jsonPath("$.institutions[0]").value(testInstitution.id().toString()));
    }

    @Test
    void should_return_all_degrees() throws Exception {
        // Create a test degree
        degrees.save(new Degree("Math", "Mathematics", List.of(testInstitution.id())));

        mockMvc.perform(
                        get("/degree/all")
                                .contentType("application/json")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[?(@.shortName == 'Math')]").exists());
    }

    @Test
    void should_update_degree() throws Exception {
        // Create a test degree
        Degree degree = degrees.save(new Degree("Phys", "Physics", List.of(testInstitution.id())));

        mockMvc.perform(
                        post("/degree/update?degreeId=" + degree.id())
                                .contentType("application/json")
                                .content(String.format("""
                                        {
                                            "shortName": "Physics",
                                            "fullName": "Physics and Astronomy",
                                            "institutionIds": ["%s", "%s"]
                                        }
                                        """, testInstitution.id(), secondTestInstitution.id()))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(degree.id().toString()))
                .andExpect(jsonPath("$.shortName").value("Physics"))
                .andExpect(jsonPath("$.fullName").value("Physics and Astronomy"))
                .andExpect(jsonPath("$.institutions.length()").value(2));
    }

    @Test
    void should_delete_degree() throws Exception {
        // Create a test degree
        Degree degree = degrees.save(new Degree("Bio", "Biology", List.of(testInstitution.id())));

        mockMvc.perform(
                        post("/degree/delete?degreeId=" + degree.id())
                )
                .andExpect(status().isOk());

        // Verify that the degree is deleted
        mockMvc.perform(get("/degree/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.shortName == 'Bio')]").doesNotExist());
    }

    @TestConfiguration
    @ComponentScan(
            basePackageClasses = {Degree.class, Institution.class},
            includeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, classes = {Stub.class})})
    static class StubConfiguration {
    }
}

