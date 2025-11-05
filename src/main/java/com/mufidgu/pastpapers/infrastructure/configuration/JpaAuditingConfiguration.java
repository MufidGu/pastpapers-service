package com.mufidgu.pastpapers.infrastructure.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Optional;

/**
 * Configuration for JPA Auditing.
 * Automatically populates createdBy and updatedBy fields using the authenticated user's information.
 * Disabled in test profile to allow stub implementations.
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@Profile("!test")
public class JpaAuditingConfiguration {

    /**
     * Provides the current auditor (user) for audit fields.
     * Extracts the user identifier from the JWT token (Google ID).
     */
    @Bean
    public AuditorAware<String> auditorProvider() {
        return new AuditorAwareImpl();
    }

    /**
     * Implementation of AuditorAware that extracts the Google ID from the JWT token.
     */
    public static class AuditorAwareImpl implements AuditorAware<String> {

        @Override
        public Optional<String> getCurrentAuditor() {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || !authentication.isAuthenticated()) {
                // For system operations (e.g., migrations), use a default auditor
                return Optional.of("system");
            }

            // Extract Google ID from JWT token
            Object principal = authentication.getPrincipal();
            if (principal instanceof Jwt jwt) {
                // Google ID is in the "sub" (subject) claim of the JWT
                String googleId = jwt.getSubject();
                if (googleId != null && !googleId.isEmpty()) {
                    return Optional.of(googleId);
                }
            }

            // Fallback to authentication name if JWT parsing fails
            String name = authentication.getName();
            if (name != null && !name.isEmpty() && !"anonymousUser".equals(name)) {
                return Optional.of(name);
            }

            // For anonymous or system operations
            return Optional.of("system");
        }
    }
}
