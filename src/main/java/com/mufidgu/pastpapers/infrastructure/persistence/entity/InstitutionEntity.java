package com.mufidgu.pastpapers.infrastructure.persistence.entity;

import com.mufidgu.pastpapers.domain.institution.Institution;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

/**
 * JPA Entity for Institution.
 * Maps to the institutions table in the database.
 */
@Entity
@Table(name = "institutions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InstitutionEntity extends AuditableEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "short_name", nullable = false, unique = true, length = 100)
    private String shortName;

    @Column(name = "full_name", nullable = false, length = 255)
    private String fullName;

    /**
     * Converts this JPA entity to a domain record.
     */
    public Institution toDomain() {
        return new Institution(id, shortName, fullName);
    }

    /**
     * Creates a JPA entity from a domain record.
     */
    public static InstitutionEntity fromDomain(Institution institution) {
        return InstitutionEntity.builder()
                .id(institution.id())
                .shortName(institution.shortName())
                .fullName(institution.fullName())
                .build();
    }
}
