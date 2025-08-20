package com.mufidgu.pastpapers.infrastructure.controller.paper;

import com.mufidgu.pastpapers.domain.common.enums.Season;
import com.mufidgu.pastpapers.domain.common.enums.Shift;
import com.mufidgu.pastpapers.domain.common.enums.Type;
import com.mufidgu.pastpapers.domain.paper.Paper;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDate;
import java.util.UUID;

public class PaperRequest {
    public UUID instructorId;

    public UUID courseId;

    public Type type;

    public UUID institutionId;

    public UUID degreeId;

    public Shift shift;

    @Range(min = 1, max = 6)
    public Integer semester;

    public Character section;

    @Range(min = 2000, max = 3000)
    public Integer year;

    public Season season;

    public LocalDate date;

    public Paper toPaper(UUID id) {
        return new Paper(
                id,
                null,
                this.instructorId,
                this.courseId,
                this.type,
                this.institutionId,
                this.degreeId,
                this.shift,
                this.semester,
                this.section,
                this.year,
                this.season,
                this.date,
                null
        );
    }
}
