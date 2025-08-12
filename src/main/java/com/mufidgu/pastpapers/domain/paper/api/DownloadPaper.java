package com.mufidgu.pastpapers.domain.paper.api;

import java.util.UUID;

public interface DownloadPaper {
    byte[] downloadPaper(UUID id);
}
