package com.mufidgu.pastpapers.domain.paper;

import com.mufidgu.pastpapers.domain.paper.api.UpdatePaper;
import com.mufidgu.pastpapers.domain.paper.enums.Season;
import com.mufidgu.pastpapers.domain.paper.enums.Shift;
import com.mufidgu.pastpapers.domain.paper.enums.Term;
import com.mufidgu.pastpapers.domain.paper.spi.Papers;
import ddd.DomainService;

import java.util.Date;
import java.util.UUID;

@DomainService
public class PaperUpdater implements UpdatePaper {

    private final Papers papers;

    public PaperUpdater(Papers papers) {
        this.papers = papers;
    }

    public Paper update(
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
    ) {
        Paper orignalPaper = papers.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Paper does not exist"));

        Paper updatedPaper = new Paper(
                orignalPaper.id(),
                instructorId,
                courseId,
                term,
                universityId,
                degreeId,
                shift,
                semester,
                section,
                year,
                season,
                date,
                orignalPaper.fileName()
        );

        updatedPaper = papers.save(updatedPaper);

        return updatedPaper;
    }
}
