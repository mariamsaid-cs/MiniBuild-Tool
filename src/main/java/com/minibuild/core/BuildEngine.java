package com.minibuild.core;

import java.nio.file.Path;
import java.util.List;

public interface BuildEngine {

    BuildReport runBuild(Path descriptorPath, List<BuildTaskType> tasks, BuildEventListener listener);

    record BuildReport(
            boolean success,
            DependencyResolver.ResolutionResult resolutionResult,
            List<BuildExecutor.TaskRecord> taskRecords,
            List<String> errors
    ) {}
}