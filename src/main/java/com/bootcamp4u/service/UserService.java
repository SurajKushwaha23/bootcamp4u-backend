package com.bootcamp4u.service;

import com.bootcamp4u.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing {@link User} entities.
 * Defines the core business logic and operations associated with user accounts.
 */
public interface UserService {

    /**
     * Registers a new user in the system.
     *
     * @param user the user entity containing registration details
     * @return the saved and persisted user entity
     * @throws Exception if validation fails or a user with the same unique constraints already exists
     */
    User registerUser(User user) throws Exception;

    /**
     * Retrieves a user by their unique identifier.
     *
     * @param id the unique ID of the user
     * @return an {@link Optional} containing the user if found, or empty if not found
     */
    Optional<User> getUserById(Long id);

    /**
     * Retrieves a user by their unique username.
     *
     * @param username the username to search for
     * @return an {@link Optional} containing the user if found, or empty if not found
     */
    Optional<User> getUserByUsername(String username);

    /**
     * Retrieves a user by their registered email address.
     *
     * @param email the email address to search for
     * @return an {@link Optional} containing the user if found, or empty if not found
     */
    Optional<User> getUserByEmail(String email);

    /**
     * Retrieves a list of all users in the system.
     * Note: For large datasets, consider using the paginated version of this method.
     *
     * @return a list of all user entities
     */
    List<User> getAllUsers();

    /**
     * Retrieves a paginated and optionally sorted list of users.
     * * @param pageable the pagination and sorting information
     * @return a page of user entities
     */
    Page<User> getAllUsers(Pageable pageable);

    /**
     * Updates an existing user's details.
     *
     * @param user the user entity containing the updated information
     * @return the updated user entity
     */
    User updateUser(User user);

    /**
     * Deletes a user from the system based on their ID.
     *
     * @param id the unique ID of the user to delete
     */
    void deleteUser(Long id);

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
    User activateUser(Long id);

    /**
     * Deactivates a user account, preventing them from logging in.
     *
     * @param id the unique ID of the user to deactivate
     * @return the updated, deactivated user entity
     */
    User deactivateUser(Long id);

    /**
     * Changes the password for a specific user.
     *
     * @param id          the unique ID of the user
     * @param newPassword the new raw password (should be encoded by the implementation before saving)
     * @return the updated user entity
     */
    User changePassword(Long id, String newPassword);

    /**
     * Initiates a password reset process (e.g., generating a token and sending an email).
     *
     * @param email the email address of the user requesting a password reset
     */
    void initiatePasswordReset(String email);
}