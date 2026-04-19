package com.bootcamp4u.controller;


import com.bootcamp4u.common.BootcampStatus;
import com.bootcamp4u.dto.request.BootcampCreateRequest;
import com.bootcamp4u.dto.request.BootcampUpdateRequest;
import com.bootcamp4u.dto.response.ApiResponse;
import com.bootcamp4u.dto.response.BootcampResponse;
import com.bootcamp4u.dto.response.PageResponse;
import com.bootcamp4u.service.BootcampService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/bootcamp")
@RequiredArgsConstructor
@Slf4j
public class BootcampController {

    private final BootcampService bootcampService;

    @PostMapping()
    public ResponseEntity<ApiResponse<BootcampResponse>> createBootcamp(
            @Valid @RequestBody final BootcampCreateRequest request, final Authentication authentication) {

        log.info("Received API request to create bootcamp: '{}'", request.getTitle());
        // 1. Delegate to the service layer (which contains the transaction and business logic)
        final BootcampResponse response = bootcampService.createBootcamp(request, authentication.getName());
        ApiResponse<BootcampResponse> apiResponse = ApiResponse.success(
                HttpStatus.CREATED.value(),
                "Bootcamp with " + request.getTitle() + " created successfully",
                response);
        // 2. Return the response
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @GetMapping(params = "!bootcampStatus")
    public ResponseEntity<PageResponse<BootcampResponse>> getAllBootcamps(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageResponse<BootcampResponse> bootcampsPage = bootcampService.getAllBootcamps(page, size);
        return ResponseEntity.ok(bootcampsPage);
    }

    // Endpoint for getting the bootcamp by UUID.

    @GetMapping ("/{uId}")
    public ResponseEntity<ApiResponse<BootcampResponse>> getBootcampById(@PathVariable UUID uId){

        log.debug("Received API request to get bootcamps with uId: {}", uId);
        BootcampResponse bootcampResponse = bootcampService.getBootcampById(uId);
        ApiResponse<BootcampResponse> apiResponse = ApiResponse.success(
                HttpStatus.OK.value(),
                "Bootcamp with uId: " + uId + " retrieved successfully",
                bootcampResponse);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);

    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<ApiResponse<BootcampResponse>> getBootcampBySlug(@PathVariable String slug){
        log.debug("Received API request to get bootcamps with Slug: {}", slug);

        BootcampResponse bootcampResponse = bootcampService.getBootcampBySlug(slug);
        ApiResponse<BootcampResponse> apiResponse = ApiResponse.success(
                HttpStatus.OK.value(),
                "Bootcamp with Slug: " + slug + " retrieved successfully",
                bootcampResponse);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse
        );
    }

    @DeleteMapping("/{uId}")
    public ResponseEntity<ApiResponse<BootcampResponse>> deleteBootcamp(@PathVariable UUID uId){
        log.debug("Received API request to delete bootcamp with uId: {}", uId);

        bootcampService.deleteBootcamp(uId);
        ApiResponse<BootcampResponse> apiResponse = ApiResponse.success(
                HttpStatus.OK.value(),
                "Bootcamp with uId: " + uId + " deleted successfully",
                null);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PutMapping("/{uId}")
   // @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    public ResponseEntity<ApiResponse<BootcampResponse>> updateBootcamp(@PathVariable UUID uId, @Valid @RequestBody BootcampUpdateRequest updateRequest){
        log.debug("Received API request to update bootcamp with uId: {}", uId);

        BootcampResponse updatedBootcamp = bootcampService.updateBootcamp(uId, updateRequest);

        ApiResponse<BootcampResponse> apiResponse = ApiResponse.success(
                HttpStatus.OK.value(),
                "Bootcamp with uId: " + uId + " updated successfully",
                updatedBootcamp
        );

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);

    }

    @GetMapping(params = "bootcampStatus")
    public ResponseEntity<PageResponse<BootcampResponse>> getBootcampsByStatus(
            @RequestParam(name = "bootcampStatus") BootcampStatus bootcampStatus,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PageResponse<BootcampResponse> bootcampsStatusResponse =
                bootcampService.getBootcampsByStatus(bootcampStatus, page, size);

        return ResponseEntity.ok(bootcampsStatusResponse);
    }

}
