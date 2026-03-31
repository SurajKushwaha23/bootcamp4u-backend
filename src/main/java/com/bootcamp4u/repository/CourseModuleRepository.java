package com.bootcamp4u.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bootcamp4u.entity.CourseModule;

import java.util.List;
import java.util.UUID;

@Repository
public interface CourseModuleRepository extends JpaRepository<CourseModule, UUID> {
    // Fetches courseModules for a specific bootcamp, ensuring they are in the correct UI order
    List<CourseModule> findAllByBootcampIdOrderBySequenceOrderAsc(UUID bootcampId);
}
