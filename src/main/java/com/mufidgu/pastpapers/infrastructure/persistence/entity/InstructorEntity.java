package com.mufidgu.pastpapers.infrastructure.persistence.entity;

import com.mufidgu.pastpapers.domain.instructor.Instructor;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * JPA Entity for Instructor.
 * Maps to the instructors table in the database.
 */
@Entity
@Table(name = "instructors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InstructorEntity extends AuditableEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "full_name", nullable = false, length = 255)
    private String fullName;

    @ManyToMany
    @JoinTable(
            name = "instructor_courses",
            joinColumns = @JoinColumn(name = "instructor_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    @Builder.Default
    private Set<CourseEntity> courses = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "instructor_institutions",
            joinColumns = @JoinColumn(name = "instructor_id"),
            inverseJoinColumns = @JoinColumn(name = "institution_id")
    )
    @Builder.Default
    private Set<InstitutionEntity> institutions = new HashSet<>();

    /**
     * Converts this JPA entity to a domain record.
     */
    public Instructor toDomain() {
        List<UUID> courseIds = courses.stream()
                .map(CourseEntity::getId)
                .collect(Collectors.toList());
        List<UUID> institutionIds = institutions.stream()
                .map(InstitutionEntity::getId)
                .collect(Collectors.toList());
        return new Instructor(id, fullName, courseIds, institutionIds);
    }

    /**
     * Creates a JPA entity from a domain record (without resolving relationships).
     */
    public static InstructorEntity fromDomain(Instructor instructor) {
        return InstructorEntity.builder()
                .id(instructor.id())
                .fullName(instructor.fullName())
                .build();
    }
}
