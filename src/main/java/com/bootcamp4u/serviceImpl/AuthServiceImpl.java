package com.bootcamp4u.serviceImpl;

import com.bootcamp4u.dto.request.LoginRequest;
import com.bootcamp4u.dto.response.LoginResponse;
import com.bootcamp4u.repository.UserRepository;
import com.bootcamp4u.security.JwtTokenProvider;
import com.bootcamp4u.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public LoginResponse login(LoginRequest request) {
        log.debug("Login attempt for username: {}", request.getUsername());

        try {
            // 1. Authenticate the user credentials
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            log.debug("Authentication result: {}", authentication.isAuthenticated());



            // 2. Extract username and roles from the authenticated object
            String username = authentication.getName();

            log.debug("Username : {}", authentication.getName());

            List<String> roles = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            log.debug("Roles : {}", roles);

            // 3. Generate the JWT token passing the extracted data
            String token = jwtTokenProvider.generateToken(username, roles);

            log.debug("Token : {}", token);

            log.info("Login successful for username: {}", username);

            return new LoginResponse(username, token);


        } catch (
                BadCredentialsException e) {
            log.warn("Invalid credentials for username: {}", request.getUsername());
            throw new RuntimeException("Invalid username or password");
        } catch (
                AuthenticationException e) {
            log.error("Authentication failed for username: {}", request.getUsername(), e);
            throw new RuntimeException("Authentication failed");
        }

    }
}
