package com.bootcamp4u.serviceImpl;

import com.bootcamp4u.dto.request.RegisterRequest;
import com.bootcamp4u.dto.response.RegisterResponse;
import com.bootcamp4u.entity.User;
import com.bootcamp4u.exception.DuplicateResourceException;
import com.bootcamp4u.mapper.UserMapper;
import com.bootcamp4u.repository.UserRepository;
import com.bootcamp4u.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private  final UserMapper userMapper;


    /**
     * Registers a new user.
     * Validation of fields is handled by Jakarta Constraints on the Entity.
     * * @param user The user object (validated via @Valid in the controller)
     * @return The saved User entity
     */
    public RegisterResponse registerUser(RegisterRequest request) {
        logger.info("Processing registration for username: {}", request.getUsername());

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("Username is already taken.");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email is already registered.");
        }

        User user = userMapper.toEntity(request);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(user);
        logger.info("User {} registered successfully with ID: {}", savedUser.getUsername(), savedUser.getId());

        return userMapper.toResponse(savedUser);
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
