package com.mufidgu.pastpapers.domain.paper.spi;

public interface FileStorage {
    void store(byte[] file, String fileName);
    byte[] retrieve(String fileName);
    void delete(String fileName);
    boolean exists(String fileName);
}
