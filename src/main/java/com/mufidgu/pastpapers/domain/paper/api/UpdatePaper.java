package com.mufidgu.pastpapers.domain.paper.api;

import com.mufidgu.pastpapers.domain.paper.Paper;

public interface UpdatePaper {
    Paper update(Paper update, String userId);
}
