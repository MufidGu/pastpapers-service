package com.mufidgu.pastpapers.infrastructure.controller.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mufidgu.pastpapers.domain.common.enums.Season;
import com.mufidgu.pastpapers.domain.common.enums.Shift;
import com.mufidgu.pastpapers.domain.user.User;
import com.mufidgu.pastpapers.infrastructure.configuration.DomainConfiguration;
import ddd.Stub;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@Import(DomainConfiguration.class)
public class UserControllerTest {

    private final String TEST_GOOGLE_ID = "1234567890";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void should_get_user_information() throws Exception {
        mockMvc.perform(
                        get("/user/get")
                                .with(jwt().jwt(jwt -> jwt.subject(TEST_GOOGLE_ID)))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.googleId").value(TEST_GOOGLE_ID));
    }

    @Test
    void should_update_user_information() throws Exception {
        UserRequest updateRequest = new UserRequest();
        updateRequest.institutionId = UUID.randomUUID();
        updateRequest.degreeId = UUID.randomUUID();
        updateRequest.sessionStartDate = LocalDate.of(2024, 2, 1);
        updateRequest.sessionStartSeason = Season.SPRING;
        updateRequest.semester = 2;
        updateRequest.section = 'B';
        updateRequest.shift = Shift.EVENING;

        mockMvc.perform(
                        put("/user/update")
                                .with(jwt().jwt(jwt -> jwt.subject(TEST_GOOGLE_ID)))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateRequest))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.googleId").value(TEST_GOOGLE_ID))
                .andExpect(jsonPath("$.institutionId").value(updateRequest.institutionId.toString()))
                .andExpect(jsonPath("$.degreeId").value(updateRequest.degreeId.toString()))
                .andExpect(jsonPath("$.sessionStartDate").value("2024-02-01"))
                .andExpect(jsonPath("$.sessionStartSeason").value("SPRING"))
                .andExpect(jsonPath("$.semester").value(2))
                .andExpect(jsonPath("$.section").value("B"))
                .andExpect(jsonPath("$.shift").value("EVENING"));
    }

    @TestConfiguration
    @ComponentScan(
            basePackageClasses = {User.class},
            includeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, classes = {Stub.class})})
    static class StubConfiguration {
    }
}
