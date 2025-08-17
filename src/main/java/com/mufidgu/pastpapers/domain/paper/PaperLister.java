package com.mufidgu.pastpapers.domain.paper;

import com.mufidgu.pastpapers.domain.paper.api.ListPaper;
import com.mufidgu.pastpapers.domain.paper.spi.Papers;
import ddd.DomainService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@DomainService
@RequiredArgsConstructor
public class PaperLister implements ListPaper {

    private final Papers papers;

    public List<Paper> listAll() {
        return papers.findAll();
    }

}
