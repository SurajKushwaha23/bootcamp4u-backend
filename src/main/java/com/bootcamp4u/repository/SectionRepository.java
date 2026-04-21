package com.bootcamp4u.repository;

import com.bootcamp4u.entity.Bootcamp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bootcamp4u.entity.Section;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SectionRepository extends JpaRepository<Section, UUID> {
    boolean existsByTitleAndBootcamp(String title, Bootcamp bootcamp);
    Optional<Section> findByIdAndBootcampId(UUID id, UUID bootcampId);
    Page<Section> findAllByBootcampId(UUID bootcampId, Pageable pageable);
    int deleteByIdAndBootcampId(UUID sectionId, UUID bootcampId);
}
