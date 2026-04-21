package com.bootcamp4u.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;


@Data
public class SectionCreateRequest {

    @NotBlank(message = "Title cannot be empty or just whitespace")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    private String title;

    // Content wasn't marked nullable = false in the entity, so it's left optional here.
    // If it is required, you can add @NotBlank(message = "Content cannot be empty")
    private String content;

    @NotNull(message = "Sequence order is required")
    @Min(value = 0, message = "Sequence order cannot be a negative number")
    private Integer sequenceOrder;
}
