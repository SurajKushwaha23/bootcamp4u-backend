package com.bootcamp4u.controller;


import com.bootcamp4u.dto.request.BootcampCreateRequest;
import com.bootcamp4u.dto.response.ApiResponse;
import com.bootcamp4u.dto.response.BootcampResponse;
import com.bootcamp4u.dto.response.PageResponse;
import com.bootcamp4u.service.BootcampService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping()
    public ResponseEntity<PageResponse<BootcampResponse>> getAllBootcamps(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageResponse<BootcampResponse> bootcampsPage = bootcampService.getAllBootcamps(page, size);
        return ResponseEntity.ok(bootcampsPage);
    }

}
