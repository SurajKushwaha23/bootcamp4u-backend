package com.bootcamp4u.exception;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * A standardized DTO for returning error details to the client.
 */
public record ErrorResponse(
        int status,
        String message,
        LocalDateTime timestamp,
        Map<String, String> validationErrors
) {}
