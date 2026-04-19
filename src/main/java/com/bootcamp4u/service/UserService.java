package com.bootcamp4u.service;

import com.bootcamp4u.dto.request.UserRegistrationRequest;
import com.bootcamp4u.dto.response.PageResponse;
import com.bootcamp4u.dto.response.UserResponse;
import com.bootcamp4u.entity.User;

import java.util.Optional;
import java.util.UUID;

/**
 * Service interface for managing {@link User} entities.
 * Defines the core business logic and operations associated with user accounts.
 */
public interface UserService {

    /**
     * Registers a new user in the system.
     *
     * @param userRequest the user entity containing registration details
     * @return the saved and persisted user entity
     * @throws Exception if validation fails or a user with the same unique constraints already exists
     */
    UserResponse registerUser(UserRegistrationRequest userRequest) throws Exception;

    /**
     * Retrieves a user by their unique identifier.
     *
     * @param id the unique ID of the user
     * @return an {@link Optional} containing the user if found, or empty if not found
     */
    UserResponse getUserById(UUID id);

    UserResponse getUserByUsername(String username);

    UserResponse getUserByEmail(String email);

    PageResponse<UserResponse> getAllUsers(int page, int size);

    // User updateUser(User user);

    void deleteUser(UUID id);

    /**
     * Checks if a specific username is already taken.
     *
     * @param username the username to check
     * @return true if the username exists, false otherwise
     */
    boolean usernameExists(String username);

    /**
     * Checks if a specific email address is already registered.
     *
     * @param email the email address to check
     * @return true if the email exists, false otherwise
     */
    boolean emailExists(String email);

    /**
     * Activates a user account, allowing them to log in and use the system.
     *
     * @param id the unique ID of the user to activate
     * @return the updated, activated user entity
     */
    User activateUser(UUID id);

    /**
     * Deactivates a user account, preventing them from logging in.
     *
     * @param id the unique ID of the user to deactivate
     * @return the updated, deactivated user entity
     */
    User deactivateUser(UUID id);

    /**
     * Changes the password for a specific user.
     *
     * @param id          the unique ID of the user
     * @param newPassword the new raw password (should be encoded by the implementation before saving)
     * @return the updated user entity
     */
    User changePassword(UUID id, String newPassword);

    /**
     * Initiates a password reset process (e.g., generating a token and sending an email).
     *
     * @param email the email address of the user requesting a password reset
     */
    void initiatePasswordReset(String email);
}