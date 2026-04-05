package com.bootcamp4u.repository;

import com.bootcamp4u.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


public interface UserRepository extends JpaRepository<User, UUID> {
    // Prevents the N+1 query problem during JWT authentication
    @EntityGraph(attributePaths = {"role"})
    Optional<User> findByEmail(String email);

    @EntityGraph(attributePaths = {"role"})
    Optional<User> findByUsername(String username);

    // Use primitive 'boolean' instead of the wrapper class 'Boolean'
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
