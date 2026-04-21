package com.bootcamp4u.mapper;

import com.bootcamp4u.dto.request.BootcampCreateRequest;
import com.bootcamp4u.dto.request.BootcampUpdateRequest;
import com.bootcamp4u.dto.response.BootcampResponse;
import com.bootcamp4u.entity.Bootcamp;
import com.bootcamp4u.entity.User;
import com.bootcamp4u.exception.OptimisticLockingException;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;


@Component
public class BootcampMapper {

    // Converts incoming DTO to Entity
    public Bootcamp toEntity(BootcampCreateRequest request, User instructor, String generatedSlug) {
        if (request == null) {
            return null;
        }

        // Build the bootcamp entity
        return Bootcamp.builder()
                .title(request.getTitle())
                .slug(generatedSlug)
                .description(request.getDescription())
                .price(request.getPrice())
                .status(request.getStatus())
                .instructor(instructor)
                .build();

    }

    // Converts Entity to outgoing DTO
    public BootcampResponse toResponse(Bootcamp entity) {
        if (entity == null) {
            return null;
        }

        return BootcampResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .slug(entity.getSlug())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .instructorId(entity.getInstructor().getId())
                .instructorName(entity.getInstructor().getUsername())
                .version(entity.getVersion())
                .build();
    }

    public Bootcamp updateEntity(Bootcamp entity, BootcampUpdateRequest updateRequest) {
        if (entity == null || updateRequest == null) {
            throw new IllegalArgumentException("Entity and BootcampUpdateRequest must not be null");
        }

        // update bootcamp now
        if(entity.getVersion() != null && !entity.getVersion().equals(updateRequest.getVersion())){
            throw new OptimisticLockingException("Version mismatch. The bootcamp has been modified by another process. Please refresh and try again.");
        }

        if (updateRequest.getDescription() != null) {
            entity.setDescription(updateRequest.getDescription());
        }

        if (updateRequest.getPrice() != null) {
            // Business Rule Example: Ensure price isn't negative
            if (updateRequest.getPrice().compareTo(BigDecimal.ZERO) < 0) {
                throw new ValidationException("Price cannot be negative");
            }
            entity.setPrice(updateRequest.getPrice());
        }

        if (updateRequest.getStatus() != null) {
            entity.setStatus(updateRequest.getStatus());
        }

       return entity;
    }

}
