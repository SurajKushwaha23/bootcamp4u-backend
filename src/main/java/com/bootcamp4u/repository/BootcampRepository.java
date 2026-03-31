package com.bootcamp4u.repository;

import com.bootcamp4u.common.BootcampStatus;
import com.bootcamp4u.entity.Bootcamp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BootcampRepository extends JpaRepository<Bootcamp, UUID> {

    // For SEO-friendly React routing (e.g., /bootcamp/mastering-spring-boot)
    Optional<Bootcamp> findBySlug(String slug);

    // Paginated list for the instructor's dashboard
    Page<Bootcamp> findAllByInstructorId(UUID instructorId, Pageable pageable);

    // Fetch only published bootcamps for the public catalog, optimized with pagination
    Page<Bootcamp> findAllByStatus(BootcampStatus status, Pageable pageable);

    // Example of a custom JPQL query for complex filtering
    @Query("SELECT b FROM Bootcamp b WHERE b.status = 'PUBLISHED' AND LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Bootcamp> searchPublishedBootcamps(@Param("keyword") String keyword, Pageable pageable);
}
