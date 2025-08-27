package com.mufidgu.pastpapers.infrastructure.entities;

import com.mufidgu.pastpapers.domain.common.enums.Season;
import com.mufidgu.pastpapers.domain.common.enums.Section;
import com.mufidgu.pastpapers.domain.common.enums.Shift;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.LocalDate;

@Entity
public class PastPapersUser {
    @Id
    private String googleId;

    @ManyToOne
    @JoinColumn(name = "institution_id")
    private Institution institution;

    @ManyToOne
    @JoinColumn(name = "degree_id")
    private Degree degree;

    private LocalDate sessionStartDate;
    private Season sessionStartSeason;
    private Integer semester;
    private Section section;
    private Shift shift;
}
