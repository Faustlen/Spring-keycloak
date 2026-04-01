package com.mentoring.keycloak;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

public class JwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private static final String clientId = "task-management-client";

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {

        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
        if (resourceAccess == null) {
            resourceAccess = Collections.emptyMap();
        }

        Collection<String> roles = Collections.emptyList();
        if (resourceAccess.containsKey(clientId) && resourceAccess.get(clientId) instanceof Map<?, ?> clientMap) {
            Object rolesObj = clientMap.get("roles");
            if (rolesObj instanceof Collection<?> roleCollection) {
                roles = (Collection<String>) roleCollection;
            }
        }

        Collection<GrantedAuthority> authorities = roles.stream()
                .map(role -> {
                    String authority = "ROLE_" + role;
                    return new SimpleGrantedAuthority(authority);
                })
                .collect(Collectors.toList());

        return new JwtAuthenticationToken(jwt, authorities);
    }
}