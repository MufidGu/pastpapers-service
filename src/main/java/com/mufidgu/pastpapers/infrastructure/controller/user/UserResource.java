package com.mufidgu.pastpapers.infrastructure.controller.user;

import com.mufidgu.pastpapers.domain.common.enums.Season;
import com.mufidgu.pastpapers.domain.common.enums.Section;
import com.mufidgu.pastpapers.domain.common.enums.Shift;
import com.mufidgu.pastpapers.domain.user.User;

import java.time.LocalDate;
import java.util.UUID;

public record UserResource(
        String googleId,
        UUID institutionId,
        UUID degreeId,
        LocalDate sessionStartDate,
        Season sessionStartSeason,
        Integer semester,
        Section section,
        Shift shift
) {
    public static UserResource from(User user) {
        return new UserResource(
                user.googleId(),
                user.institutionId(),
                user.degreeId(),
                user.sessionStartDate(),
                user.sessionStartSeason(),
                user.semester(),
                user.section(),
                user.shift()
        );
    }
}
