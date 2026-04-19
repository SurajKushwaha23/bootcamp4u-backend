package com.bootcamp4u.config;

import com.bootcamp4u.entity.User;
import com.bootcamp4u.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableJpaAuditing
public class ApplicationConfig {

    // Pulls from application.yml: app.cors.allowed-origins=http://localhost:3000,http://localhost:5173
    @Value("${app.cors.allowed-origins}")
    private List<String> allowedOrigins;

    private final UserRepository userRepository;

    /**
     * 1. UserDetailsService
     * Spring Security uses this to find the user in your database during authentication.
     * We use a lambda to implement the single loadUserByUsername method.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            // 1. Fetch your custom user entity
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found in the database"));

            // 2. Map it to Spring Security's UserDetails object
            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword()) // Ensure this is hashed in the DB!
                    .roles(String.valueOf(user.getRole())) // You can also map dynamic roles from your user entity here
                    .build();
        };
    }

    /**
     * 2. PasswordEncoder
     * Tells Spring Security how to hash and verify passwords.
     * BCrypt is the industry standard for this.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 3. AuthenticationProvider
     * This is the data access object (DAO) responsible for fetching the user details
     * and encoding/verifying passwords. We plug in our custom beans from above.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * 4. AuthenticationManager
     * The main interface we used back in your AuthServiceImpl to actually trigger the authentication.
     * Spring handles the complex creation of this; we just expose it as a bean.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) {
        try{
            return config.getAuthenticationManager();
        }catch (Exception e){
            throw new RuntimeException("AuthenticationConfiguration is failed", e);
        }

    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(allowedOrigins);
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        // Performance improvement: Cache the preflight OPTIONS request for 1 hour
        configuration.setMaxAge(3600L);

        // Optional: Allow the frontend to read specific response headers
        configuration.setExposedHeaders(List.of("Authorization", "Content-Type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
