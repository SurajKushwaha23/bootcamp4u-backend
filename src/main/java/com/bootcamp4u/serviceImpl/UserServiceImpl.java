package com.bootcamp4u.serviceImpl;

import com.bootcamp4u.dto.request.RegisterRequest;
import com.bootcamp4u.dto.response.PageResponse;
import com.bootcamp4u.dto.response.UserResponse;
import com.bootcamp4u.entity.User;
import com.bootcamp4u.exception.DuplicateResourceException;
import com.bootcamp4u.exception.InvalidInputException;
import com.bootcamp4u.exception.ResourceNotFoundException;
import com.bootcamp4u.mapper.UserMapper;
import com.bootcamp4u.repository.UserRepository;
import com.bootcamp4u.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private  final UserMapper userMapper;


    /**
     * Registers a new user.
     * Validation of fields is handled by Jakarta Constraints on the Entity.
     * * @param user The user object (validated via @Valid in the controller)
     * @return The saved User entity
     */
    public UserResponse registerUser(RegisterRequest request) {
        log.info("Processing registration for username: {}", request.getUsername());

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("Username is already taken.");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email is already registered.");
        }

        User user = userMapper.toEntity(request);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(user);
        log.info("User {} registered successfully with ID: {}", savedUser.getUsername(), savedUser.getId());

        return userMapper.toResponse(savedUser);
    }

    @Override
    public UserResponse getUserById(UUID id) {
        log.debug("Fetching user details with id: {}", id);

        // 1. Retrieve the user from the repository, throwing a specific exception if not found
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Failed to find user details with id: {}", id);
                    return new ResourceNotFoundException("User not found with id: " + id);
                });

        return userMapper.toResponse(user);

    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserByUsername(String username) {
        // 1. Guard clause for bad input
        if (!StringUtils.hasText(username)) {
            log.warn("Username is null or empty");
            throw new InvalidInputException("Username is null or empty");

        }

        log.debug("Fetching user details by username: {}", username);

        // 2. Functional approach to Optional handling
        return userRepository.findByEmail(username)
                .map(userMapper::toResponse)
                .orElseThrow(() -> {
            log.warn("Failed to find user details by username: {}", username);
            return new ResourceNotFoundException("User not found with username: " + username);
        });

    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserByEmail(String email) {

        // 1. Guard clause for bad input
        if (!StringUtils.hasText(email)) {
            log.warn("Email is null or empty");
            throw new InvalidInputException("Email is null or empty");
        }

        log.debug("Fetching user details by email: {}", email);

       // 2. Functional approach to Optional handling
      return userRepository.findByEmail(email)
              .map(userMapper::toResponse)
              .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<UserResponse> getAllUsers(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        // 1. Fetch and map the data exactly as you were doing before
        Page<UserResponse> userPage = userRepository.findAll(pageable)
                .map(userMapper::toResponse);

        // 2. Wrap the mapped page and its content into your custom DTO
        return new PageResponse<>(userPage, userPage.getContent());
    }


    @Override
    @Transactional
    public void deleteUser(UUID id) {

        log.debug("Deleting user with id: {}", id);
        // 1. Verify the user exists before attempting deletion
        if (!userRepository.existsById(id)) {
           log.warn("User with id: {} does not exist", id);
           throw new ResourceNotFoundException("User not with given id: " + id);
        }

        // 2. Perform the deletion (which triggers your @SQLDelete soft-delete)
        userRepository.deleteById(id);

        // 3. Log success only after we know it worked
        log.info("User with id: {} deleted successfully", id);

    }

    @Override
    public boolean usernameExists(String username) {
        return false;
    }

    @Override
    public boolean emailExists(String email) {
        return false;
    }

    @Override
    public User activateUser(UUID id) {
        return null;
    }

    @Override
    public User deactivateUser(UUID id) {
        return null;
    }

    @Override
    public User changePassword(UUID id, String newPassword) {
        return null;
    }

    @Override
    public void initiatePasswordReset(String email) {

    }
}
