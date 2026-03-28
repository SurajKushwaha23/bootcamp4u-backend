package com.bootcamp4u.config;

import com.bootcamp4u.security.CustomAccessDeniedHandler;
import com.bootcamp4u.security.JwtAuthenticationEntryPoint;
import com.bootcamp4u.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http     // 1. Disable CSRF (Cross-Site Request Forgery)
                // We disable this because we are using stateless JWTs, not session cookies.
                .csrf(AbstractHttpConfigurer::disable)

                // 2. Exception Handling
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(customAccessDeniedHandler)
                )

                // 3. Configure endpoint authorization rules
                .authorizeHttpRequests(auth -> auth
                        // Allow anyone to access the login and registration endpoints
                        .requestMatchers("/api/v1/auth/**", "/error").permitAll()
                        // Any other request must be authenticated
                        .anyRequest().authenticated()
                )

                // 4. Configure session management to be stateless
                // This tells Spring not to create or use HTTP sessions to store the user's security context.
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // No sessions
                )

                // 5. Set the authentication provider (which handles fetching the user and comparing passwords)
                .authenticationProvider(authenticationProvider)

                // 6. Add our custom JWT filter BEFORE the standard Spring Security password filter
                // This ensures our token is checked and the user is authenticated before Spring tries to block the request.
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();

    }


}
