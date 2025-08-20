package com.mufidgu.pastpapers.infrastructure.controller.user;

import com.mufidgu.pastpapers.domain.common.enums.Season;
import com.mufidgu.pastpapers.domain.common.enums.Shift;
import com.mufidgu.pastpapers.domain.user.User;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDate;
import java.util.UUID;

public class UserRequest {
    public UUID institutionId;

    public UUID degreeId;

    public LocalDate sessionStartDate;

    public Season sessionStartSeason;

    @Range(min = 1, max = 6)
    public Integer semester;

    public Character section;

    public Shift shift;

    public User toUser(String googleId) {
        return new User(
                googleId,
                this.institutionId,
                this.degreeId,
                this.sessionStartDate,
                this.sessionStartSeason,
                this.semester,
                this.section,
                this.shift
        );
    }
}
