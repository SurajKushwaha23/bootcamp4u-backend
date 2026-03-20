package com.bootcamp4u.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtTokenProvider jwtTokenProvider;

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
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        try {
            // 1. Extract the JWT from the HTTP request
            String jwtToken = getJwtTokenFromHeader(request);

            // 2. Validate the token
            if (StringUtils.hasText(jwtToken) && jwtTokenProvider.validateToken(jwtToken)) {

                // 3. Extract data from the valid token
                String username = jwtTokenProvider.getUsernameFromToken(jwtToken);
                List<String> roles = jwtTokenProvider.getRolesFromToken(jwtToken);

                // 4. Convert string roles to Spring Security GrantedAuthorities
                List<SimpleGrantedAuthority> authorities = roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

                // 5. Create the Authentication object directly (No DB hit!)
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(username, null, authorities);

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 5. Save the Authentication to the SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authentication);

            }

        } catch (Exception ex) {
            logger.error("Could not set user authentication in security context", ex);
        }

        // 6. Continue the request flow
        filterChain.doFilter(request, response);

    }
}
