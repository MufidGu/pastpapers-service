package com.mufidgu.pastpapers.infrastructure.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import org.springframework.data.jpa.domain.AbstractAuditable;

import java.util.List;
import java.util.UUID;

@Entity
public class Degree extends AbstractAuditable<PastPapersUser, UUID> {
    private String shortName;
    private String fullName;

    @ManyToMany
    @JoinTable(name="DEGREE_INSTITUTIONS")
    private List<Institution> institutions;
}
