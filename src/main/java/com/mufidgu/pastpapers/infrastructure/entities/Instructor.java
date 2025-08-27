package com.mufidgu.pastpapers.infrastructure.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import org.springframework.data.jpa.domain.AbstractAuditable;

import java.util.List;
import java.util.UUID;

@Entity
public class Instructor extends AbstractAuditable<PastPapersUser, UUID> {
    private String fullName;

    @ManyToMany
    @JoinTable(name="INSTRUCTOR_COURSES")
    private List<Course> courses;
}
