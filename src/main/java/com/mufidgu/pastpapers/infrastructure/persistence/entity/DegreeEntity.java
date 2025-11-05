package com.mufidgu.pastpapers.infrastructure.persistence.entity;

import com.mufidgu.pastpapers.domain.degree.Degree;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * JPA Entity for Degree.
 * Maps to the degrees table in the database.
 */
@Entity
@Table(name = "degrees")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DegreeEntity extends AuditableEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "short_name", nullable = false, unique = true, length = 100)
    private String shortName;

    @Column(name = "full_name", nullable = false, length = 255)
    private String fullName;

    @ManyToMany
    @JoinTable(
            name = "degree_institutions",
            joinColumns = @JoinColumn(name = "degree_id"),
            inverseJoinColumns = @JoinColumn(name = "institution_id")
    )
    @Builder.Default
    private Set<InstitutionEntity> institutions = new HashSet<>();

    /**
     * Converts this JPA entity to a domain record.
     */
    public Degree toDomain() {
        List<UUID> institutionIds = institutions.stream()
                .map(InstitutionEntity::getId)
                .collect(Collectors.toList());
        return new Degree(id, shortName, fullName, institutionIds);
    }

    /**
     * Creates a JPA entity from a domain record (without resolving institutions).
     */
    public static DegreeEntity fromDomain(Degree degree) {
        return DegreeEntity.builder()
                .id(degree.id())
                .shortName(degree.shortName())
                .fullName(degree.fullName())
                .build();
    }
}
