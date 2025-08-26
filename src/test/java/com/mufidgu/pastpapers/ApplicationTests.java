package com.mufidgu.pastpapers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mufidgu.pastpapers.infrastructure.configuration.security.JwtRoleConverter;
import com.mufidgu.pastpapers.infrastructure.controller.institution.InstitutionRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtRoleConverter jwtRoleConverter;

    // NOTE: Set SPRING_PROFILES_ACTIVE=test in env
    @Value("${app.security.admin-google-ids}")
    private List<String> adminGoogleIds;

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

        Jwt adminJwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim("sub", adminGoogleIds.getFirst())
                .build();

        Collection<GrantedAuthority> authorities = Objects.requireNonNull(
                jwtRoleConverter.convert(adminJwt)
        ).getAuthorities();

        mockMvc.perform(post("/institution/add")
                        .with(jwt().authorities(authorities))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());
    }

    @Test
    void whenNotAdminRole_thenForbidden() throws Exception {
        InstitutionRequest req = new InstitutionRequest();
        req.shortName = "MIT";
        req.fullName = "Massachusetts Institute of Technology";

        Jwt userJwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim("sub", "non-admin-user-id")
                .build();

        Collection<GrantedAuthority> authorities = Objects.requireNonNull(
                jwtRoleConverter.convert(userJwt)
        ).getAuthorities();

        mockMvc.perform(post("/institution/add")
                        .with(jwt().authorities(authorities))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isForbidden());
    }
}