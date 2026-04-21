package com.bootcamp4u.controller;

import com.bootcamp4u.dto.request.SectionCreateRequest;
import com.bootcamp4u.dto.response.ApiResponse;
import com.bootcamp4u.dto.response.PageResponse;
import com.bootcamp4u.dto.response.SectionResponse;
import com.bootcamp4u.service.SectionService;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/bootcamp/{bootcampId}/section")
@RequiredArgsConstructor
public class SectionController {

    private final SectionService sectionService;

    @PostMapping
    public ResponseEntity<ApiResponse<SectionResponse>> createSection(
            @PathVariable final UUID bootcampId,
            @Valid @RequestBody final SectionCreateRequest request) {

        log.debug("REST request to create Section for Bootcamp ID: {}", bootcampId);

        SectionResponse sectionResponse = sectionService.createSection(request, bootcampId);

        ApiResponse<SectionResponse> apiResponse = ApiResponse.success(
                HttpStatus.CREATED.value(),
                "Section with title '" + request.getTitle() + "' created successfully for Bootcamp ID: " + bootcampId,
                sectionResponse
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @GetMapping("/{sectionId}")
    public ResponseEntity<ApiResponse<SectionResponse>> getSectionById(
            @NonNull @PathVariable UUID sectionId, @PathVariable UUID bootcampId) {

     log.debug("REST request to get section by id : {}", sectionId);

        SectionResponse sectionResponse = sectionService.getSectionById(sectionId, bootcampId);
        ApiResponse<SectionResponse> apiResponse = ApiResponse.success(
                HttpStatus.OK.value(),
                "Section with id '" + sectionId + "' retrieved successfully",
                sectionResponse
        );
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);

    }

    @GetMapping
    public ResponseEntity<Page<SectionResponse>> getAllSectionsByBootcampId(
            @PathVariable UUID bootcampId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.debug("REST request to get all sections for bootcamp id: {}, page: {}, size: {}", bootcampId, page, size);

        final Page<SectionResponse> allSectionsByBootcampId = sectionService.getAllSectionsByBootcampId(bootcampId, page, size);

        return ResponseEntity.ok(allSectionsByBootcampId);

    }

    @DeleteMapping("/{sectionId}")
    public ResponseEntity<ApiResponse<SectionResponse>> deleteSection(
            @PathVariable UUID sectionId,
            @PathVariable UUID bootcampId) {

        log.debug("REST request to delete section with id : {}", sectionId);

        sectionService.deleteSection(sectionId, bootcampId);
        ApiResponse<SectionResponse> apiResponse = ApiResponse.success(
                HttpStatus.OK.value(),
                "Section is deleted successfully",
                null
        );
        return ResponseEntity.ok(apiResponse);


    }
}
