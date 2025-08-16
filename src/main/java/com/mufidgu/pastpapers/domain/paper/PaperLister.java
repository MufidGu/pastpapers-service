package com.mufidgu.pastpapers.domain.paper;

import com.mufidgu.pastpapers.domain.paper.api.ListPaper;
import com.mufidgu.pastpapers.domain.paper.spi.Papers;
import ddd.DomainService;

import java.util.List;

@DomainService
public class PaperLister implements ListPaper {

    private final Papers papers;

    public PaperLister(Papers papers) {
        this.papers = papers;
    }

    public List<Paper> listAll() {
        return papers.findAll();
    }

}
