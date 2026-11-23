package com.minibuild.core;

public interface VersionMatcher {

    /**
     * Compares two version strings numerically (e.g., "1.2.0" < "1.10.0").
     */
    int compareVersions(String versionA, String versionB);

    /**
     * Checks whether a version satisfies an exact version or range like "[1.0,2.0)".
     */
    boolean matchesRange(String version, String versionOrRange);
}