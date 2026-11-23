package com.minibuild.core;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public interface DependencyResolver {

    ResolutionResult resolve(DescriptorParser.ProjectDescriptor descriptor)
            throws CycleDetectedException;

    record ResolvedNode(
            String moduleOrArtifactName,
            String selectedVersion,
            Path artifactPath,
            List<String> dependencies,
            boolean hasConflict,
            String selectionExplanation
    ) {}

    record ResolutionResult(
            List<String> topologicalModuleOrder,
            Map<String, ResolvedNode> resolvedNodes,
            List<String> conflictSummaries
    ) {}
}