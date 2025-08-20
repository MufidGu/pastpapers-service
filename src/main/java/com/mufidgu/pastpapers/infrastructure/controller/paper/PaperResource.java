package com.mufidgu.pastpapers.infrastructure.controller.paper;

import com.mufidgu.pastpapers.domain.common.enums.Season;
import com.mufidgu.pastpapers.domain.common.enums.Shift;
import com.mufidgu.pastpapers.domain.common.enums.Type;
import com.mufidgu.pastpapers.domain.paper.Paper;

import java.time.LocalDate;
import java.util.UUID;

public record PaperResource(
        UUID id,
        UUID instructorId,
        UUID courseId,
        Type type,
        UUID institutionId,
        UUID degreeId,
        Shift shift,
        Integer semester,
        Character section,
        Integer year,
        Season season,
        LocalDate date
) {

    public static PaperResource from(Paper paper) {
        return new PaperResource(
                paper.id(),
                paper.instructorId(),
                paper.courseId(),
                paper.type(),
                paper.institutionId(),
                paper.degreeId(),
                paper.shift(),
                paper.semester(),
                paper.section(),
                paper.year(),
                paper.season(),
                paper.date()
        );
    }
}
