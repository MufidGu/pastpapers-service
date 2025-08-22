package com.mufidgu.pastpapers.infrastructure.configuration.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class JwtRoleConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final JwtGrantedAuthoritiesConverter defaultConverter = new JwtGrantedAuthoritiesConverter();
    private final SecurityProperties securityProperties;

    public JwtRoleConverter(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    public AbstractAuthenticationToken convert(@Nullable Jwt jwt) {

        if (jwt  == null) {
            return null;
        }

        Collection<GrantedAuthority> defaultAuthorities = defaultConverter.convert(jwt);

        String googleId = jwt.getClaimAsString("sub");

        Collection<GrantedAuthority> authorities = defaultAuthorities;

        if (securityProperties.getAdminGoogleIds() != null &&
                securityProperties.getAdminGoogleIds().contains(googleId)) {
            authorities = Stream.concat(
                    authorities.stream(),
                    Stream.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
            ).collect(Collectors.toList());
        }

        return new JwtAuthenticationToken(jwt, authorities);
    }
}