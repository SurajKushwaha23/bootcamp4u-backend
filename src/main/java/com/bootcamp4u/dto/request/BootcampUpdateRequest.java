package com.bootcamp4u.dto.request;

import com.bootcamp4u.common.BootcampStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BootcampUpdateRequest {

    @NotNull(message = "Version is required to prevent concurrent modification conflicts")
    private Long version;

    @NotBlank(message = "Description must not be empty")
    private String description;

    @NotNull(message = "Price must not be null")
    @DecimalMin(value = "0.0", inclusive = true, message = "Price cannot be negative")
    private BigDecimal price;

    @NotNull(message = "Bootcamp status must be specified")
    private BootcampStatus status;
}
