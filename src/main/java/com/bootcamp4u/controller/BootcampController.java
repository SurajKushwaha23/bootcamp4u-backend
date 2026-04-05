package com.bootcamp4u.controller;


import com.bootcamp4u.dto.request.BootcampCreateRequest;
import com.bootcamp4u.dto.response.BootcampResponse;
import com.bootcamp4u.service.BootcampService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${app.bootcamps.path}")
@RequiredArgsConstructor
@Slf4j
public class BootcampController {

    private final BootcampService bootcampService;


    @PostMapping
    public ResponseEntity<BootcampResponse> createBootcamp(
            @Valid @RequestBody final BootcampCreateRequest request, final Authentication authentication) {

        log.info("Received API request to create bootcamp: '{}'", request.getTitle());



        // 1. Delegate to the service layer (which contains the transaction and business logic)
        final BootcampResponse response = bootcampService.createBootcamp(request, authentication.getName());


        return ResponseEntity.ok(response);

    }

}
