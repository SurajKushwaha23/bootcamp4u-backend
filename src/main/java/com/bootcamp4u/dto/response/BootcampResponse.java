package com.bootcamp4u.dto.response;

import com.bootcamp4u.common.BootcampStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BootcampResponse {

    private UUID id;
    private String title;
    private String slug;
    private String description;
    private BigDecimal price;
    private BootcampStatus status;

    // Flattened Instructor details
    private UUID instructorId;
    // Assuming your User entity has a getFullName() or getUsername() method
    private String instructorName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Note: If you want to include course modules, you should create a
    // CourseModuleResponse DTO and include it as a list here:
    // private List<CourseModuleResponse> courseModules;
}