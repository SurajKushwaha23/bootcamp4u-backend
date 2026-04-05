package com.bootcamp4u.service;

import com.bootcamp4u.common.BootcampStatus;
import com.bootcamp4u.dto.request.BootcampCreateRequest;
import com.bootcamp4u.dto.response.BootcampResponse;
import com.bootcamp4u.entity.Bootcamp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface BootcampService {

    BootcampResponse createBootcamp(BootcampCreateRequest request, String authenticatedUserIdentifier);

   /* Bootcamp getBootcampById(UUID id);

    Bootcamp getBootcampBySlug(String slug);

    Page<Bootcamp> getAllBootcamps(Pageable pageable);

    Page<Bootcamp> getBootcampsByInstructor(UUID instructorId, Pageable pageable);

    Page<Bootcamp> getBootcampsByStatus(BootcampStatus status, Pageable pageable);

    Page<Bootcamp> searchPublishedBootcamps(String keyword, Pageable pageable);

    Bootcamp updateBootcamp(UUID id, Bootcamp bootcampDetails);

    void deleteBootcamp(UUID id);*/
}
