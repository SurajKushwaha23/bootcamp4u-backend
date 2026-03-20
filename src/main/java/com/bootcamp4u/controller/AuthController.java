package com.bootcamp4u.controller;

import com.bootcamp4u.dto.request.LoginRequest;
import com.bootcamp4u.dto.request.RegisterRequest;
import com.bootcamp4u.dto.response.LoginResponse;
import com.bootcamp4u.dto.response.RegisterResponse;
import com.bootcamp4u.service.AuthService;
import com.bootcamp4u.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    // Service inject
    private final UserService  userService;
    private final AuthService  authService;


    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) throws Exception {

         RegisterResponse response = userService.registerUser(request);
         return ResponseEntity.ok(response);

    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {

        // Pass the request body down to the service layer we built earlier
        LoginResponse response = authService.login(request);

        // Return an HTTP 200 OK along with the JWT and success message
        return ResponseEntity.ok(response);
    }



}
