package com.bootcamp4u.serviceImpl;

import com.bootcamp4u.entity.User;
import com.bootcamp4u.exception.DuplicateResourceException;
import com.bootcamp4u.repository.UserRepository;
import com.bootcamp4u.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registers a new user.
     * Validation of fields is handled by Jakarta Constraints on the Entity.
     * * @param user The user object (validated via @Valid in the controller)
     * @return The saved User entity
     */
    public User registerUser(User user) {
        logger.info("Processing registration for username: {}", user.getUsername());

        // Check uniqueness (Logic that requires DB access stays in the service)
        if (userRepository.existsByUsername(user.getUsername())) {
            logger.error("Registration failed: Username {} already exists", user.getUsername());
            throw new DuplicateResourceException("Username is already in use");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            logger.error("Registration failed: Email {} already exists", user.getEmail());
            throw new DuplicateResourceException("Email is already in use");
        }

        // Secure the password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Logic for default state
        if (user.getIsActive() == null) {
            user.setIsActive(true);
        }

        User savedUser = userRepository.save(user);
        logger.info("User {} registered successfully with ID: {}", savedUser.getUsername(), savedUser.getId());

        return savedUser;
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        return Optional.empty();
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public List<User> getAllUsers() {
        return List.of();
    }

    @Override
    public Page<User> getAllUsers(Pageable pageable) {
        return null;
    }

    @Override
    public User updateUser(User user) {
        return null;
    }

    @Override
    public void deleteUser(Long id) {

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
    public User activateUser(Long id) {
        return null;
    }

    @Override
    public User deactivateUser(Long id) {
        return null;
    }

    @Override
    public User changePassword(Long id, String newPassword) {
        return null;
    }

    @Override
    public void initiatePasswordReset(String email) {

    }
}
