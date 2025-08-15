package com.mufidgu.pastpapers.domain.paper.api;

import com.mufidgu.pastpapers.domain.paper.Paper;
import com.mufidgu.pastpapers.domain.paper.enums.Season;
import com.mufidgu.pastpapers.domain.paper.enums.Shift;
import com.mufidgu.pastpapers.domain.paper.enums.Term;

import java.util.Date;
import java.util.UUID;

public interface UpdatePaper {
    Paper update(
            UUID id,
            UUID instructorId,
            UUID courseId,
            Term term,
            UUID universityId,
            UUID degreeId,
            Shift shift,
            Integer semester,
            Character section,
            Date year,
            Season season,
            Date date
    );
}
