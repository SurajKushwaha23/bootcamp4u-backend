package com.bootcamp4u.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * Helper method to extract the token from the Authorization header.
     * Expects format: "Bearer eyJhbGciOiJIUzI1NiJ9..."
     */

    private String getJwtTokenFromHeader(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if(StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        return null;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            String jwtToken = getJwtTokenFromHeader(request);

            if (jwtToken != null && jwtTokenProvider.validateToken(jwtToken)) {

                String username = jwtTokenProvider.getUsernameFromToken(jwtToken);
                List<String> roles = jwtTokenProvider.getClaimFromToken(jwtToken, claims -> claims.get("roles", List.class));

                // 4. Convert string roles to Spring Security GrantedAuthorities
                List<SimpleGrantedAuthority> authorities = roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

                // 5. Create the Authentication object directly (No DB hit!)
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(username, null, authorities);

            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
