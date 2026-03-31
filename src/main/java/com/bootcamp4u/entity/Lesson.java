package com.bootcamp4u.entity;

import com.bootcamp4u.common.ContentType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "lessons")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class Lesson extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courseModule_id", nullable = false)
    private CourseModule courseModule;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "content_type", nullable = false)
    private ContentType contentType;

    @Column(name = "content_url")
    private String contentUrl;

    @Column(name = "sequence_order", nullable = false)
    private Integer sequenceOrder;
}
