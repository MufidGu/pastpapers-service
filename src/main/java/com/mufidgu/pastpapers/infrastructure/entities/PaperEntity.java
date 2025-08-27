package com.mufidgu.pastpapers.infrastructure.entities;

import com.mufidgu.pastpapers.domain.common.enums.Season;
import com.mufidgu.pastpapers.domain.common.enums.Section;
import com.mufidgu.pastpapers.domain.common.enums.Shift;
import com.mufidgu.pastpapers.domain.common.enums.Type;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.AbstractAuditable;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "papers")
public class PaperEntity extends AbstractAuditable<UserEntity, UUID> {

    @ManyToOne
    @JoinColumn(name = "user_google_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "instructor_id")
    private InstructorEntity instructor;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private CourseEntity course;

    @ManyToOne
    @JoinColumn(name = "institution_id")
    private InstitutionEntity institution;

    @ManyToOne
    @JoinColumn(name = "degree_id")
    private DegreeEntity degree;

    private Type type;
    private Shift shift;
    private Integer semester;
    private Section section;
    private Integer year;
    private Season season;
    private LocalDate date;
    private String fileName;
}
