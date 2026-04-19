package com.bootcamp4u.mapper;

import com.bootcamp4u.dto.request.UserRegistrationRequest;
import com.bootcamp4u.dto.response.UserResponse;
import com.bootcamp4u.entity.User;
import org.springframework.stereotype.Component;


@Component
public class UserMapper {

    // Maps the incoming Request DTO to your User Entity
    public User toEntity(UserRegistrationRequest request) {
        if (request == null) {
            return null;
        }

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setRole(request.getRole());

        // isActive defaults to true in your entity, no need to set it unless requested
        return user;
    }

    // Maps your User Entity back to a safe Response DTO
    public UserResponse toResponse(User user) {
        if (user == null) {
            return null;
        }

        return UserResponse.builder()
                .id(user.getId())
                .firstname(user.getFirstName())
                .lastname(user.getLastName())
                .username(user.getUsername())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole())
                .isActive(user.getIsActive())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}