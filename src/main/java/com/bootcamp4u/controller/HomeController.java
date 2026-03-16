package com.bootcamp4u.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class HomeController {

    @GetMapping("/login")
    public ResponseEntity<String> login() {
        return ResponseEntity.ok("Login request");
    }

    @GetMapping("/register")
    public ResponseEntity<String> register() {
        return ResponseEntity.ok("register request");
    }

}
