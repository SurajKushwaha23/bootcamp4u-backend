package com.bootcamp4u.serviceImpl;

import com.bootcamp4u.common.Role;
import com.bootcamp4u.dto.request.BootcampCreateRequest;
import com.bootcamp4u.dto.response.BootcampResponse;
import com.bootcamp4u.entity.Bootcamp;
import com.bootcamp4u.entity.User;
import com.bootcamp4u.exception.AuthorizationException;
import com.bootcamp4u.exception.DuplicateResourceException;
import com.bootcamp4u.exception.ResourceNotFoundException;
import com.bootcamp4u.mapper.BootcampMapper;
import com.bootcamp4u.repository.BootcampRepository;
import com.bootcamp4u.repository.UserRepository;
import com.bootcamp4u.service.BootcampService;
import com.bootcamp4u.util.SlugUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BootcampServiceImpl implements BootcampService {

    private final BootcampRepository bootcampRepository;
    private final UserRepository userRepository;
    private final BootcampMapper bootcampMapper;

    @Override
    @Transactional
    public BootcampResponse createBootcamp(final BootcampCreateRequest request, String authenticatedUserIdentifier) {

        // 1. Generate the slug from the Title
        final String generatedSlug = SlugUtil.toSlug(request.getTitle());
        log.debug("Initiating bootcamp creation for slug: {}", generatedSlug);

        // Optional: Early fail for better UX, but not reliable for concurrency
        if (bootcampRepository.existsBySlug(generatedSlug)) {
            log.warn("Bootcamp creation failed. Slug already exists: {}", generatedSlug);
            throw new DuplicateResourceException("A bootcamp with this slug already exists: " + generatedSlug);
        }

        // 2. Fetch Required Dependencies and Validate Role
        final User instructor = userRepository.findByUsername(authenticatedUserIdentifier)
                .orElseThrow(() -> {
                    log.error("Authenticated user not found in database for username: {}", authenticatedUserIdentifier);
                    return new ResourceNotFoundException("Instructor profile not found.");
                });

        // Ensure the user actually has the authority to be an instructor
        if (!instructor.getRole().equals(Role.INSTRUCTOR)) {
            log.warn("User {} attempted to create a bootcamp without instructor privileges.", instructor.getId());
            throw new AuthorizationException("The provided user is not an instructor.");
        }

        // 3. Map, Persist, and Return
        final Bootcamp bootcampToSave = bootcampMapper.toEntity(request, instructor, generatedSlug);

        try {
            final Bootcamp savedBootcamp = bootcampRepository.save(bootcampToSave);
            log.info("Successfully created bootcamp with ID: {}", savedBootcamp.getId());
            return bootcampMapper.toResponse(savedBootcamp);

        } catch (DataIntegrityViolationException e) {
            // This catches the database unique constraint violation if two requests try to save the same slug concurrently
            log.error("Database constraint violation for slug: {}", generatedSlug, e);
            throw new DuplicateResourceException("A bootcamp with this slug already exists: " + generatedSlug);
        }
    }
}
