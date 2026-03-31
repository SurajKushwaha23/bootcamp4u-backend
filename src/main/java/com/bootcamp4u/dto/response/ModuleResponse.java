package com.bootcamp4u.dto.response;


import lombok.*;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModuleResponse {

    private UUID id;
    private String title;
    private String content;
    private Integer orderIndex; // Assuming you have an ordering field

    // We only need the ID of the parent bootcamp, not the whole object
    private UUID bootcampId;
}