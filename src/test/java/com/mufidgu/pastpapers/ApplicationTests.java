package com.mufidgu.pastpapers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mufidgu.pastpapers.infrastructure.controller.institution.InstitutionRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void contextLoads() {
    }

    @Test
    void whenNotAuthenticated_thenUnauthorized() throws Exception {
        mockMvc.perform(get("/paper/list"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void whenJwtAuthenticated_thenOk() throws Exception {
        mockMvc.perform(get("/paper/list")
                        .with(jwt()
                                .jwt(jwt -> {
                                    jwt.claim("sub", "1234567890");
                                    jwt.claim("email", "user@example.com");
                                    jwt.claim("scope", "paper.read");
                                })))
                .andExpect(status().isOk());
    }

    @Test
    void whenAdminRole_thenCanAddInstitution() throws Exception {
        InstitutionRequest req = new InstitutionRequest();

        req.shortName = "MIT";
        req.fullName = "Massachusetts Institute of Technology";

        mockMvc.perform(post("/institution/add")
                        .with(jwt().authorities(AuthorityUtils.createAuthorityList("ROLE_ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());
    }

    @Test
    void whenNotAdminRole_thenForbidden() throws Exception {
        InstitutionRequest req = new InstitutionRequest();

        req.shortName = "MIT";
        req.fullName = "Massachusetts Institute of Technology";

        mockMvc.perform(post("/institution/add")
                        .with(jwt().authorities(AuthorityUtils.createAuthorityList("ROLE_USER")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isForbidden());
    }
}
