package com.bootcamp4u.dto.response;

import com.bootcamp4u.common.BootcampStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
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
    private UUID instructorId;
    private String instructorName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}