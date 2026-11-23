package com.minibuild.core;

import java.nio.file.Path;
import java.time.Instant;
import java.util.Optional;

public interface BuildCache {

    /**
     * Computes a SHA-256 digest for a module's inputs/sources/outputs.
     */
    String computeSha256(Path rootPath);

    Optional<CacheEntry> getEntry(String moduleName);

    void putEntry(String moduleName, CacheEntry entry);

    void save(Path cacheFile);

    void load(Path cacheFile);

    record CacheEntry(
            String moduleName,
            String inputHash,
            String outputHash,
            Instant updatedAt
    ) {}
}