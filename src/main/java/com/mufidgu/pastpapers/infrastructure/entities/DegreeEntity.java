package com.mufidgu.pastpapers.infrastructure.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.AbstractAuditable;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "degrees")
public class DegreeEntity extends AbstractAuditable<UserEntity, UUID> {
    private String shortName;
    private String fullName;

    @ManyToMany
    @JoinTable(name="DEGREE_INSTITUTIONS")
    private List<InstitutionEntity> institutions;
}
