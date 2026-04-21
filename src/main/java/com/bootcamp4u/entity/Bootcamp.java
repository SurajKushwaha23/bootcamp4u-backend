package com.bootcamp4u.entity;

import com.bootcamp4u.common.BootcampStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "bootcamps")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"instructor", "sections"})
//@SQLDelete(sql = "UPDATE bootcamps SET is_deleted = true WHERE id=? AND version=?")
//@SQLRestriction("is_deleted=false")
public class Bootcamp extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instructor_id", nullable = false)
    private User instructor;

    @Column(nullable = false)
    private String title;

    // The 'slug' is a perfect Business Key (natural identifier)
    // because it is both required and guaranteed unique.
    @Column(nullable = false, unique = true)
    private String slug;

    @Lob // Marks it as a Large Object
    @Column(columnDefinition = "LONGTEXT") // Explicitly tells MySQL to use LONGTEXT
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BootcampStatus status;

    @Column(name = "is_deleted", nullable = false)
    @Builder.Default
    private boolean isDeleted = false;

    @OneToMany(mappedBy = "bootcamp", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Section> sections = new ArrayList<>();

    // ==========================================
    //       UTILITY METHODS (SYNCHRONIZATION)
    // ==========================================

    public void addCourseModule(Section module) {
        sections.add(module);
        module.setBootcamp(this); // Syncs the database foreign key
    }

    public void removeCourseModule(Section module) {
        sections.remove(module);
        module.setBootcamp(null); // Removes the foreign key link
    }

    // ==========================================
    //    BUSINESS KEY EQUALS & HASHCODE
    // ==========================================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        // Using 'instanceof' is safer in JPA because Hibernate
        // creates proxy classes that might fail a strict getClass() check.
        if (!(o instanceof Bootcamp bootcamp)) return false;

        // Two bootcamps are considered equal if they share the same unique slug
        return slug != null && slug.equals(bootcamp.getSlug());
    }

    @Override
    public int hashCode() {
        // Hash based exclusively on the immutable business key
        return Objects.hash(slug);
    }
}