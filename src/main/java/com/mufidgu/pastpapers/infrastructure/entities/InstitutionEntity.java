package com.mufidgu.pastpapers.infrastructure.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.AbstractAuditable;

import java.util.UUID;
@Getter
@Setter
@Entity
@Table(name = "institutions")
public class InstitutionEntity extends AbstractAuditable<UserEntity, UUID> {
    private String shortName;
    private String fullName;
}
