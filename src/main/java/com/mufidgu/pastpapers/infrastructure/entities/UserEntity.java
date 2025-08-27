package com.mufidgu.pastpapers.infrastructure.entities;

import com.mufidgu.pastpapers.domain.common.enums.Season;
import com.mufidgu.pastpapers.domain.common.enums.Section;
import com.mufidgu.pastpapers.domain.common.enums.Shift;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    private String googleId;

    @ManyToOne
    @JoinColumn(name = "institution_id")
    private InstitutionEntity institution;

    @ManyToOne
    @JoinColumn(name = "degree_id")
    private DegreeEntity degree;

    private LocalDate sessionStartDate;
    private Season sessionStartSeason;
    private Integer semester;
    private Section section;
    private Shift shift;
}
