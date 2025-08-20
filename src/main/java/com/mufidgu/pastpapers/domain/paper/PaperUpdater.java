package com.mufidgu.pastpapers.domain.paper;

import com.mufidgu.pastpapers.domain.common.exception.NotFoundException;
import com.mufidgu.pastpapers.domain.paper.api.UpdatePaper;
import com.mufidgu.pastpapers.domain.paper.spi.Papers;
import ddd.DomainService;
import lombok.RequiredArgsConstructor;

@DomainService
@RequiredArgsConstructor
public class PaperUpdater implements UpdatePaper {

    private final Papers papers;

    public Paper update(Paper update) {
        Paper originalPaper = papers.findById(update.id())
                .orElseThrow(() -> new NotFoundException("Paper does not exist"));

        Paper updatedPaper = originalPaper.updateWith(update);

        return papers.save(updatedPaper);
    }
}
