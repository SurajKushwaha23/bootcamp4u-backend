package com.bootcamp4u.dto.request;

import com.bootcamp4u.common.BootcampStatus;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BootcampRequest {

    @NotNull(message = "Instructor ID is required")
    private UUID instructorId; // Matches the UUID type in BaseEntity

    @NotBlank(message = "Title is required")
    @Size(min = 5, max = 255)
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Price is required")
    @PositiveOrZero
    private BigDecimal price;

    @NotNull(message = "Status is required")
    private BootcampStatus status;

    // Allows creating courseModules at the same time as the bootcamp
    private List<ModuleRequest> modules;
}