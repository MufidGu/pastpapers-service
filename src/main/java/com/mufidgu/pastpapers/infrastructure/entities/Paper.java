package com.mufidgu.pastpapers.infrastructure.entities;

import com.mufidgu.pastpapers.domain.common.enums.Season;
import com.mufidgu.pastpapers.domain.common.enums.Section;
import com.mufidgu.pastpapers.domain.common.enums.Shift;
import com.mufidgu.pastpapers.domain.common.enums.Type;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.springframework.data.jpa.domain.AbstractAuditable;

import java.time.LocalDate;
import java.util.UUID;

@Entity
public class Paper extends AbstractAuditable<PastPapersUser, UUID> {

    @ManyToOne
    @JoinColumn(name = "user_google_id")
    private PastPapersUser user;

    @ManyToOne
    @JoinColumn(name = "instructor_id")
    private Instructor instructor;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "institution_id")
    private Institution institution;

    @ManyToOne
    @JoinColumn(name = "degree_id")
    private Degree degree;

    private Type type;
    private Shift shift;
    private Integer semester;
    private Section section;
    private Integer year;
    private Season season;
    private LocalDate date;
    private String fileName;
}
