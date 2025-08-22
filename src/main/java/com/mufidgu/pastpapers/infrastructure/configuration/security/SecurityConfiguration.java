package com.mufidgu.pastpapers.infrastructure.configuration.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtRoleConverter jwtRoleConverter) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers(
                                "/course/add",
                                "/course/update",
                                "/course/delete",
                                "/degree/add",
                                "/degree/update",
                                "/degree/delete",
                                "/institution/add",
                                "/institution/update",
                                "/institution/delete",
                                "/instructor/add",
                                "/instructor/update",
                                "/instructor/delete"
                        ).hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(
                        oauth2 -> oauth2.jwt(
                                jwtConfigurer -> jwtConfigurer.jwtAuthenticationConverter(jwtRoleConverter)
                        )
                );
        return http.build();
    }

}
