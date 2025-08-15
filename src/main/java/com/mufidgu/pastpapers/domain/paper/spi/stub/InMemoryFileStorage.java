package com.mufidgu.pastpapers.domain.paper.spi.stub;

import com.mufidgu.pastpapers.domain.paper.File;
import com.mufidgu.pastpapers.domain.paper.spi.FileStorage;

import java.io.IOException;
import java.util.HashMap;

public class InMemoryFileStorage implements FileStorage {
    HashMap<String, File> files = new HashMap<>();

    public void store(byte[] file, String fileName) {
        files.put(fileName, new File(fileName, file));
    }

    public byte[] retrieve(String fileName) throws IOException {
        File file = files.get(fileName);
        if (file != null) {
            return file.contents();
        }
        throw new IOException("File does not exist");
    }

    public void delete(String fileName) {
        files.remove(fileName);
    }
}
