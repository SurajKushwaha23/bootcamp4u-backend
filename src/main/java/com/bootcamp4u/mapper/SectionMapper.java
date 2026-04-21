package com.bootcamp4u.mapper;

import com.bootcamp4u.dto.request.SectionCreateRequest;
import com.bootcamp4u.dto.response.SectionResponse;
import com.bootcamp4u.entity.Bootcamp;
import com.bootcamp4u.entity.Section;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.UUID;

@Component
public class SectionMapper {

    public Section toSectionEntity(SectionCreateRequest sectionCreateRequest, Bootcamp bootcamp) {

        if (sectionCreateRequest == null || bootcamp == null) {
            return null;
        }


        return Section.builder()
                .bootcamp(bootcamp)
                .title(sectionCreateRequest.getTitle())
                .content(sectionCreateRequest.getContent())
                .sequenceOrder(sectionCreateRequest.getSequenceOrder())
                .lessons(new ArrayList<>()) // Initialize empty lessons list
                .build();
    }

    public SectionResponse toSectionResponse(Section section) {
        if (section == null) {
            return null;
        }

        UUID uuid = section.getBootcamp() != null ? section.getBootcamp().getId() : null;

        return SectionResponse.builder()
                .id(section.getId())
                .title(section.getTitle())
                .content(section.getContent())
                .sequenceOrder(section.getSequenceOrder())
                .bootcampId(uuid)
                .createdAt(section.getCreatedAt())
                .updatedAt(section.getUpdatedAt())
                .version(section.getVersion())
                .build();
    }
}
