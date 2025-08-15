package com.mufidgu.pastpapers.domain.paper.api;

import com.mufidgu.pastpapers.domain.paper.File;

import java.util.UUID;

public interface DownloadPaper {
    File downloadPaper(UUID id);
}
