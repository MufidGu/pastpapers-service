package com.mufidgu.pastpapers.domain.paper.spi;

import java.io.IOException;

public interface FileStorage {
    void store(byte[] file, String fileName);
    byte[] retrieve(String fileName) throws IOException;
    void delete(String fileName);
}
