package com.bootcamp4u.service;

import com.bootcamp4u.dto.request.SectionCreateRequest;
import com.bootcamp4u.dto.response.SectionResponse;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface SectionService {

    /**
     * Creates a new section for a specific bootcamp.
     *
     * @param bootcampId The ID of the bootcamp to which the section will belong.
     * @param sectionCreateRequest The payload containing section details.
     * @return SectionResponse containing the created section data.
     * @throws com.bootcamp4u.exception.ResourceNotFoundException if the bootcamp is not found.
     */
    SectionResponse createSection(SectionCreateRequest sectionCreateRequest, UUID bootcampId);

    /**
     * Retrieves a specific section by its unique ID.
     *
     * @param sectionId The ID of the section.
     * @return SectionResponse containing the section data.
     * @throws com.bootcamp4u.exception.ResourceNotFoundException if the section does not exist.
     */
    SectionResponse getSectionById(UUID sectionId, UUID bootcampId);

    /**
     * Retrieves a paginated list of all sections belonging to a specific bootcamp.
     * * Note: In production, returning a Page is safer than a List to prevent
     * memory overload if a bootcamp grows to have hundreds of sections/lessons.
     *
     * @param bootcampId The ID of the bootcamp.
     * @param page The page number to retrieve (0-based).
     * @param size The number of sections per page.
     * @return A Page of SectionResponse objects.
     */
    Page<SectionResponse> getAllSectionsByBootcampId(UUID bootcampId, int page, int size);

    /**
     * Retrieves all sections for a bootcamp ordered by their sequence order.
     * Useful for UI rendering where pagination isn't required but order is strict.
     * @param bootcampId The ID of the bootcamp.
     * @return A list of SectionResponse objects sorted by sequenceOrder.
     */
    List<SectionResponse> getOrderedSectionsByBootcampId(Long bootcampId);

    /**
     * Updates an existing section's details.
     * @param sectionId The ID of the section to update.
     * @param request   The payload containing the updated details.
     * @return SectionResponse containing the updated section data.
     * @throws com.bootcamp4u.exception.ResourceNotFoundException if the section does not exist.
     */
   // SectionResponse updateSection(Long sectionId, SectionUpdateRequest request);

    /**
     * Deletes a section by its ID.
     *
     * @param sectionId The ID of the section to delete.
     * @throws com.bootcamp4u.exception.ResourceNotFoundException if the section does not exist.
     */
    void deleteSection(UUID sectionId, UUID bootcampId);
}