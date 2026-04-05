package com.bootcamp4u.controller;

import com.bootcamp4u.dto.response.ApiResponse;
import com.bootcamp4u.dto.response.PageResponse;
import com.bootcamp4u.dto.response.UserResponse;
import com.bootcamp4u.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("get-all-users")
    public ResponseEntity<PageResponse<UserResponse>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size ) {

        PageResponse<UserResponse> usersPage = userService.getAllUsers(page, size);
        return ResponseEntity.ok(usersPage);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> deleteUser(@PathVariable UUID id) {
        // 1. Log the incoming REST request
        log.info("REST request to delete User with ID: {}", id);

        // 2. Call your newly fortified service method
        userService.deleteUser(id);

        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.success(HttpStatus.OK.value(), "User deleted successfully", null)
        );

    }
}
