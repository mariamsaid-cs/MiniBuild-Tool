package com.minibuild.core;

import java.util.List;

public class CycleDetectedException extends RuntimeException {
    private final List<String> cyclePath;

    public CycleDetectedException(List<String> cyclePath) {
        super("Dependency cycle detected: " + String.join(" -> ", cyclePath));
        this.cyclePath = List.copyOf(cyclePath);
    }

    public List<String> getCyclePath() {
        return cyclePath;
    }
}