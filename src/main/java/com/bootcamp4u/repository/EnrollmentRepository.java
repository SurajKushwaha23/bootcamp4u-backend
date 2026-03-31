package com.bootcamp4u.repository;

import com.bootcamp4u.common.EnrollmentStatus;
import com.bootcamp4u.entity.Enrollment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, UUID> {

    // Checks if a student is already enrolled (prevents double purchasing)
    boolean existsByStudentIdAndBootcampId(UUID studentId, UUID bootcampId);

    // Fetches a specific enrollment
    Optional<Enrollment> findByStudentIdAndBootcampId(UUID studentId, UUID bootcampId);

    // Gets all active enrollments for a student's dashboard
    // Using @EntityGraph to solve the N+1 problem by eagerly fetching the bootcamp data in a single join
    @EntityGraph(attributePaths = {"bootcamp"})
    Page<Enrollment> findAllByStudentIdAndStatus(UUID studentId, EnrollmentStatus status, Pageable pageable);
}