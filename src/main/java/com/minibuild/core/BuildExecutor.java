package com.minibuild.core;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public interface BuildExecutor {

    List<TaskRecord> executePlan(
            DescriptorParser.ProjectDescriptor descriptor,
            DependencyResolver.ResolutionResult resolution,
            List<BuildTaskType> tasksToRun,
            BuildCache cache,
            BuildEventListener listener
    );

    enum TaskStatus {
        SKIPPED_UP_TO_DATE,
        RUNNING,
        PASSED,
        FAILED
    }

    record TaskRecord(
            String moduleName,
            BuildTaskType taskType,
            String threadName,
            Instant startTime,
            Instant endTime,
            TaskStatus status,
            String details
    ) {}
}