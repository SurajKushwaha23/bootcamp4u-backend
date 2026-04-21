package com.bootcamp4u.serviceImpl;

import com.bootcamp4u.dto.request.SectionCreateRequest;
import com.bootcamp4u.dto.response.SectionResponse;
import com.bootcamp4u.entity.Bootcamp;
import com.bootcamp4u.entity.Section;
import com.bootcamp4u.exception.DuplicateResourceException;
import com.bootcamp4u.exception.ResourceNotFoundException;
import com.bootcamp4u.mapper.SectionMapper;
import com.bootcamp4u.repository.BootcampRepository;
import com.bootcamp4u.repository.SectionRepository;
import com.bootcamp4u.service.SectionService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SectionServiceImpl implements SectionService {

    private final SectionRepository sectionRepository;
    private final SectionMapper sectionMapper;
    private final BootcampRepository bootcampRepository;

    @Override
    @Transactional
    public SectionResponse createSection(final SectionCreateRequest sectionCreateRequest, UUID bootcampId) {

        // 1. Basic Object Validation
        if (sectionCreateRequest == null) {
            throw new IllegalArgumentException("SectionCreateRequest cannot be null");
        }

        if (bootcampId == null) {
            throw new IllegalArgumentException("bootcampId cannot be null");
        }

        log.debug("Attempting to create a new section for bootcamp id: {}", bootcampId); // Fixed logging message

        // 2. Fetch Parent Entity
        Bootcamp bootcamp = bootcampRepository.findById(bootcampId).orElseThrow(
                () -> new ResourceNotFoundException("Bootcamp with id " + bootcampId + " not found")
        );

        // 3. Business Validation (e.g., Check for duplicates)
        if (sectionRepository.existsByTitleAndBootcamp(sectionCreateRequest.getTitle(), bootcamp)) {
            throw new DuplicateResourceException("A section with this title already exists in this bootcamp");
        }

        // 2. Map and save section in db
        final Section section = sectionMapper.toSectionEntity(sectionCreateRequest, bootcamp);
        final Section savedSection = sectionRepository.save(section);

        log.info("Successfully created section with id {} for bootcamp id {}", savedSection.getId(), bootcampId);

        // 4. Return response
        return sectionMapper.toSectionResponse(savedSection);
    }

    @Override
    @Transactional(readOnly = true)
    public SectionResponse getSectionById(@NonNull UUID sectionId, @NonNull UUID bootcampId) {

        log.debug("Attempting to get section with id {}", sectionId);

        // 2. find section with section id
        Section section = sectionRepository.findByIdAndBootcampId(sectionId, bootcampId).orElseThrow(
                () -> new ResourceNotFoundException("Section with id " + sectionId + " not found in bootcamp with id " + bootcampId)
        );

        //3. return section if found
        return sectionMapper.toSectionResponse(section);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SectionResponse> getAllSectionsByBootcampId(@NonNull UUID bootcampId, int page, int size) {

        log.debug("Attempting to get all sections for bootcamp id {} with pagination - page: {}, size: {}", bootcampId, page, size);

        // 1. Create the pagination request
        Pageable pageable = PageRequest.of(page, size);

        // 2. Fetch the paginated data
        Page<Section> sectionPage = sectionRepository.findAllByBootcampId(bootcampId, pageable);

        // 3. Map entities to DTOs
        return sectionPage.map(sectionMapper::toSectionResponse);

    }

    @Override
    public List<SectionResponse> getOrderedSectionsByBootcampId(Long bootcampId) {
        return List.of();
    }

    @Override
    @Transactional
    public void deleteSection(@NonNull UUID sectionId, @NonNull UUID bootcampId) {

        log.debug("Attempting to delete section {} for bootcamp {}", sectionId, bootcampId);

        //1. first we need to check bootcamp exists or not, if not we can throw ResourceNotFoundException for bootcamp
        if (!bootcampRepository.existsById(bootcampId)) {
            throw new ResourceNotFoundException("Bootcamp with id " + bootcampId + " not found");
        }

        final int deletedCount = sectionRepository.deleteByIdAndBootcampId(sectionId, bootcampId);

        if (deletedCount == 0) {
            throw new ResourceNotFoundException(
                    String.format("Section with id %s not found in bootcamp with id %s", sectionId, bootcampId)
            );
        }

        log.info("Successfully deleted section with id {}", sectionId);
    }
}
