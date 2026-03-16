package com.bootcamp4u.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.WeakKeyException;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Service
public class JwtTokenProvider {

    /*
     * Logger instance for logging JWT operations
     * */
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);


    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expiration;

    private JwtParser jwtParser;

    @PostConstruct
    public void init() {

        try {
            if (secretKey == null || secretKey.isBlank()) {
                throw new IllegalArgumentException("JWT Secret Key is missing! Check your application.properties or environment variables.");
            }

            byte[] keyBytes = Decoders.BASE64.decode(secretKey);
            SecretKey key = Keys.hmacShaKeyFor(keyBytes);

            this.jwtParser = Jwts.parser().verifyWith(key).build();

            logger.info("JWT Parser initialized successfully");

        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);

        } catch (WeakKeyException e) {
            logger.error("CRITICAL: JWT Secret key is too short. It must be at least 32 characters (256 bits).");
            throw e; // Stop the application startup

        } catch (Exception e) {
            logger.error("CRITICAL: Failed to initialize JWT Parser: {}", e.getMessage());
            throw new RuntimeException("Application cannot start without a valid JWT configuration", e);
        }
    }

    private Claims getAllClaimsFromToken(String token) {
        return jwtParser.parseSignedClaims(token).getPayload();
    }

    public String generateToken(String username, List<String> roles) {

        logger.info("Generating token for user {}", username);

        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }

        try {
            Date now = new Date();
            Date expiryDate = new Date(now.getTime() + expiration);

            byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
            SecretKey key = Keys.hmacShaKeyFor(keyBytes);

            return Jwts.builder()
                    .subject(username)
                    .claim("roles", roles)
                    .issuedAt(now)
                    .expiration(expiryDate)
                    .signWith(key, Jwts.SIG.HS256)
                    .compact();

        } catch (Exception e) {
            logger.error("Failed to generate JWT for user: {}", username, e);
            throw new RuntimeException("Internal server error during token generation");
        }

    }

    public boolean validateToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

            Jwts.parser()
                    .verifyWith(key) // Modern verification
                    .build()         // Creates an immutable, thread-safe parser
                    .parseSignedClaims(token); // Better than generic 'parse'

            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("Expired JWT token: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT token claims string is empty: {}", e.getMessage());
        } catch (SignatureException e) {
            logger.error("JWT signature validation failed: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Error validating JWT token: {}", e.getMessage());
        }

        return false;
    }


    public String getUsernameFromToken(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    public Date getExpirationDateFromToken(String token) {
        return getAllClaimsFromToken(token).getExpiration();
    }

    /**
     * Generic helper to extract any claim without re-parsing the token multiple times.
     */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = jwtParser.parseSignedClaims(token).getPayload();
        return claimsResolver.apply(claims);
    }


}
