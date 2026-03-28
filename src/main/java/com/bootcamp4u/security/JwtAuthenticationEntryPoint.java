package com.bootcamp4u.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/*
* The AuthenticationEntryPoint intercepts this and allows you to return a clean, standard JSON 401 Unauthorized response instead.
* */

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                         @NonNull AuthenticationException authException) throws IOException, ServletException {

        // 1. Set the response status to 401 Unauthorized

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // 2. Set the content type to JSON
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        // 3. Create a structured JSON payload
        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        body.put("error", "Unauthorized");
        body.put("message", authException.getMessage()); // Usually "Full authentication is required..."
        body.put("path", request.getServletPath());

        // 4. Write the JSON object to the HTTP response
        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), body);


    }
}
