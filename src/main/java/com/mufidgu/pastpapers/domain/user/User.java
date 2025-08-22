package com.mufidgu.pastpapers.domain.user;

import com.mufidgu.pastpapers.domain.common.enums.Season;
import com.mufidgu.pastpapers.domain.common.enums.Section;
import com.mufidgu.pastpapers.domain.common.enums.Shift;

import java.time.LocalDate;
import java.util.UUID;

public record User(
        String googleId,
        UUID institutionId,
        UUID degreeId,
        LocalDate sessionStartDate,
        Season sessionStartSeason,
        Integer semester,
        Section section,
        Shift shift
) {
    public static User createFromGoogleId(String googleId) {
        return new User(
                googleId,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

    public User updateWith(User update) {
        return new User(
                this.googleId,
                update.institutionId,
                update.degreeId,
                update.sessionStartDate,
                update.sessionStartSeason,
                update.semester,
                update.section,
                update.shift
        );
    }
}
