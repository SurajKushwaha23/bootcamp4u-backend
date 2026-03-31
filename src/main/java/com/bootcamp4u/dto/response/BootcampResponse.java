package com.bootcamp4u.dto.response;

import com.bootcamp4u.common.BootcampStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BootcampResponse {

    private UUID id;         // The auto-generated UUID
    private String title;
    private String slug;      // Backend-generated slug
    private String description;
    private BigDecimal price;
    private BootcampStatus status;

    private UUID instructorId;
    private String instructorName;

    private List<ModuleResponse> modules;

    // Auditing fields from BaseEntity
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long version;     // Useful for the frontend to track state
}