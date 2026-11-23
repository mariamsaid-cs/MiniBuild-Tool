package com.minibuild.core;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface DescriptorParser {

    ProjectDescriptor parse(Path descriptorFile) throws DescriptorParseException;

    record DependencySpec(
            String name,
            String versionOrRange,
            Set<String> exclusions
    ) {}

    record ModuleSpec(
            String name,
            Path sourceDir,
            Path testDir,
            Path outputDir,
            List<DependencySpec> dependencies,
            List<String> customTasks
    ) {}

    record ProjectDescriptor(
            String projectName,
            Path localRepositoryDir,
            ConflictStrategy conflictStrategy,
            List<ModuleSpec> modules,
            Map<String, String> customTaskCommands
    ) {}
}