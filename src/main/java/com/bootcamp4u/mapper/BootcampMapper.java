package com.bootcamp4u.mapper;

import com.bootcamp4u.dto.request.BootcampCreateRequest;
import com.bootcamp4u.dto.response.BootcampResponse;
import com.bootcamp4u.entity.Bootcamp;
import com.bootcamp4u.entity.User;
import org.springframework.stereotype.Component;

@Component
public class BootcampMapper {

    // Converts incoming DTO to Entity
    public Bootcamp toEntity(BootcampCreateRequest request, User instructor, String generatedSlug) {
        if (request == null) {
            return null;
        }

        return Bootcamp.builder()
                .title(request.getTitle())
                .slug(generatedSlug)
                .description(request.getDescription())
                .price(request.getPrice())
                .status(request.getStatus())
                .instructor(instructor)
                // courseModules and isDeleted are handled by @Builder.Default in your entity
                .build();
    }

    // Converts Entity to outgoing DTO
    public BootcampResponse toResponse(Bootcamp entity) {
        if (entity == null) {
            return null;
        }

        return BootcampResponse.builder()
                // Assuming BaseEntity has getId() returning UUID
                .id(entity.getId())
                .title(entity.getTitle())
                .slug(entity.getSlug())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .instructorId(entity.getInstructor().getId())
                // Replace .getUsername() with whatever name field your User entity uses
                .instructorName(entity.getInstructor().getUsername())
                .build();
    }
}
