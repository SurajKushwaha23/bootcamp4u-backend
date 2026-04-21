package com.bootcamp4u.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SectionResponse {

    private UUID id;

    private String title;

    private String content;

    private Integer sequenceOrder;

    private UUID bootcampId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Long version;
}
