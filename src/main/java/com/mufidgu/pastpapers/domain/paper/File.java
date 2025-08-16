package com.mufidgu.pastpapers.domain.paper;

public record File(
        String filename,
        byte[] contents
) {
}
