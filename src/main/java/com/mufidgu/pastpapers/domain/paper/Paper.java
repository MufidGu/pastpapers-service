package com.mufidgu.pastpapers.domain.paper;

import com.mufidgu.pastpapers.domain.common.enums.Season;
import com.mufidgu.pastpapers.domain.common.enums.Shift;
import com.mufidgu.pastpapers.domain.common.enums.Type;

import java.time.LocalDate;
import java.util.UUID;

public record Paper(
        UUID id,
        String userId,
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
        LocalDate date,
        String fileName
) {
    public Paper(
            String userId,
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
            LocalDate date,
            String fileName
    ) {
        this(
                UUID.randomUUID(),
                userId,
                instructorId,
                courseId,
                type,
                institutionId,
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

    public static Paper createFromFileName(String fileName, String userId) {
        return new Paper(
                userId,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                fileName
        );
    }

    public Paper updateWith(Paper updatedUser) {
        return new Paper(
                this.id,
                this.userId,
                updatedUser.instructorId,
                updatedUser.courseId,
                updatedUser.type,
                updatedUser.institutionId,
                updatedUser.degreeId,
                updatedUser.shift,
                updatedUser.semester,
                updatedUser.section,
                updatedUser.year,
                updatedUser.season,
                updatedUser.date,
                this.fileName
        );
    }
}
