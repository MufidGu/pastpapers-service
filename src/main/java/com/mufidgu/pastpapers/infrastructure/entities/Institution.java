package com.mufidgu.pastpapers.infrastructure.entities;

import jakarta.persistence.Entity;
import org.springframework.data.jpa.domain.AbstractAuditable;

import java.util.UUID;

@Entity
public class Institution extends AbstractAuditable<PastPapersUser, UUID> {
    private String shortName;
    private String fullName;
}
