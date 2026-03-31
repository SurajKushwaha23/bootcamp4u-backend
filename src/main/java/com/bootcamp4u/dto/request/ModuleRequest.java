package com.bootcamp4u.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModuleRequest {

    @NotBlank(message = "CourseModule title is required")
    private String title;

    @NotBlank(message = "CourseModule content is required")
    private String content;

    @NotNull(message = "Order index is required to maintain sequence")
    private Integer orderIndex;

    // Note: We deliberately DO NOT include bootcampId here.
    // If nested in BootcampRequest, the parent is implicit.
    // If used in a separate endpoint, the bootcampId should come from the URL path.
}

