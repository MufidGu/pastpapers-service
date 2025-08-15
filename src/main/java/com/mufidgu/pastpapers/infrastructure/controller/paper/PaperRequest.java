package com.mufidgu.pastpapers.infrastructure.controller.paper;

import com.mufidgu.pastpapers.domain.paper.enums.Season;
import com.mufidgu.pastpapers.domain.paper.enums.Shift;
import com.mufidgu.pastpapers.domain.paper.enums.Term;

import java.util.Date;
import java.util.UUID;

public class PaperRequest {
    public UUID instructorId;
    public UUID courseId;
    public Term term;
    public UUID universityId;
    public UUID degreeId;
    public Shift shift;
    public Integer semester;
    public Character section;
    public Date year;
    public Season season;
    public Date date;
}
