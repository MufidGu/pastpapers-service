package com.mufidgu.pastpapers.domain.paper;

import com.mufidgu.pastpapers.domain.paper.enums.Season;
import com.mufidgu.pastpapers.domain.paper.enums.Shift;
import com.mufidgu.pastpapers.domain.paper.enums.Type;

import java.util.Date;
import java.util.UUID;

public record Paper(
        UUID id,
        UUID instructorId,
        UUID courseId,
        Type type,
        UUID universityId,
        UUID degreeId,
        Shift shift,
        Integer semester,
        Character section,
        Integer year,
        Season season,
        Date date,
        String fileName
) {
    public Paper(UUID instructorId,
                 UUID courseId,
                 Type type,
                 UUID universityId,
                 UUID degreeId,
                 Shift shift,
                 Integer semester,
                 Character section,
                 Integer year,
                 Season season,
                 Date date,
                 String fileName
    ) {
        this(
                UUID.randomUUID(),
                instructorId,
                courseId,
                type,
                universityId,
                degreeId,
                shift,
                semester,
                section,
                year,
                season,
                date,
                fileName
        );
    }
}
