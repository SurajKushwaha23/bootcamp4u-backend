package com.bootcamp4u.config;

import com.bootcamp4u.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final UserRepository userRepository;

    /**
     * 1. UserDetailsService
     * Spring Security uses this to find the user in your database during authentication.
     * We use a lambda to implement the single loadUserByUsername method.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByUsername(username)
                // Make sure your UserRepository has an Optional<User> findByUsername(String username) method!
                .orElseThrow(() -> new UsernameNotFoundException("User not found in the database"));
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
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}
