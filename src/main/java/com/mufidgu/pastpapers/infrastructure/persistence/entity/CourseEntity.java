package com.mufidgu.pastpapers.infrastructure.persistence.entity;

import com.mufidgu.pastpapers.domain.course.Course;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * JPA Entity for Course.
 * Maps to the courses table in the database.
 */
@Entity
@Table(name = "courses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseEntity extends AuditableEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "short_name", nullable = false, unique = true, length = 100)
    private String shortName;

    @Column(name = "full_name", nullable = false, length = 255)
    private String fullName;

    @ManyToMany
    @JoinTable(
            name = "course_degrees",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "degree_id")
    )
    @Builder.Default
    private Set<DegreeEntity> degrees = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "course_institutions",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "institution_id")
    )
    @Builder.Default
    private Set<InstitutionEntity> institutions = new HashSet<>();

    /**
     * Converts this JPA entity to a domain record.
     */
    public Course toDomain() {
        List<UUID> degreeIds = degrees.stream()
                .map(DegreeEntity::getId)
                .collect(Collectors.toList());
        List<UUID> institutionIds = institutions.stream()
                .map(InstitutionEntity::getId)
                .collect(Collectors.toList());
        return new Course(id, shortName, fullName, degreeIds, institutionIds);
    }

    /**
     * Creates a JPA entity from a domain record (without resolving relationships).
     */
    public static CourseEntity fromDomain(Course course) {
        return CourseEntity.builder()
                .id(course.id())
                .shortName(course.shortName())
                .fullName(course.fullName())
                .build();
    }
}
