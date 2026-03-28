package com.bootcamp4u.controller;

import com.bootcamp4u.dto.request.LoginRequest;
import com.bootcamp4u.dto.request.RegisterRequest;
import com.bootcamp4u.dto.response.ApiResponse;
import com.bootcamp4u.dto.response.LoginResponse;
import com.bootcamp4u.dto.response.UserResponse;
import com.bootcamp4u.service.AuthService;
import com.bootcamp4u.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    // Service inject
    private final UserService  userService;
    private final AuthService  authService;


    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> register(@Valid @RequestBody RegisterRequest request) throws Exception {

         UserResponse response = userService.registerUser(request);

        log.info("User registered successfully: {}", response.getUsername());
        return ResponseEntity
                .status(HttpStatus.CREATED) // Correct HTTP 201 status
                .body(ApiResponse.success(
                        HttpStatus.CREATED.value(),
                        "User registered successfully",
                        response
                ));

    }


    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {

        // Pass the request body down to the service layer we built earlier
        LoginResponse loginResponse = authService.login(request);

        String message = String.format("%s authenticated successfully", loginResponse.getUsername());

        log.info("User authenticated successfully: {}", loginResponse.getUsername());

        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), message,  loginResponse));
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id The ID of the user to retrieve
     * @return ResponseEntity containing the UserResponse and a 200 OK status
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable UUID id) {

        // The service layer handles the logic and throws ResourceNotFoundException if missing
        UserResponse response = userService.getUserById(id);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(
                HttpStatus.CREATED.value(),
                "User found successfully",
                response
        ));
    }

}
