package com.bootcamp4u.service;

import com.bootcamp4u.common.BootcampStatus;
import com.bootcamp4u.dto.request.BootcampCreateRequest;
import com.bootcamp4u.dto.request.BootcampUpdateRequest;
import com.bootcamp4u.dto.response.BootcampResponse;
import com.bootcamp4u.dto.response.PageResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;


public interface BootcampService {

    BootcampResponse createBootcamp(BootcampCreateRequest request, String authenticatedUserIdentifier);

    PageResponse<BootcampResponse> getAllBootcamps(int page, int size);

    BootcampResponse getBootcampById(@NotNull UUID uId);

    BootcampResponse getBootcampBySlug(String slug);

    void deleteBootcamp(UUID id);

    BootcampResponse updateBootcamp(@NotNull UUID id, @Valid @NotNull BootcampUpdateRequest updateRequest);

    PageResponse<BootcampResponse> getBootcampsByStatus(BootcampStatus status, int page, int size);

}
