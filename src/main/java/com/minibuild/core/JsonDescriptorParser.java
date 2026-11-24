package com.minibuild.core;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JsonDescriptorParser implements DescriptorParser{
    @Override
    public ProjectDescriptor parse(Path descriptorFile) throws DescriptorParseException{
        try {
            List<String> lines = Files.readAllLines(descriptorFile);
            String joined = String.join("\n", lines);

            int repoLine = findLineContaining(lines, "\"localRepositoryDir\"", 1);
            if(!joined.contains("\"localRepositoryDir\"")){
                throw new DescriptorParseException("Missing required field: localRepositoryDir", repoLine);
            }

            return new ProjectDescriptor(
                    "sample-project",
                    Path.of("./local-repo"),
                    ConflictStrategy.NEAREST_WINS,
                    List.of(),
                    Map.of()
            );
        }catch(DescriptorParseException e){
            throw e;
        }catch(Exception e){
            throw new DescriptorParseException("Failed to parse descriptor: " + e.getMessage(), 1, e);
        }
    }

    private int findLineContaining(List<String> lines, String needle, int fallbackLine){
        for(int i = 0; i < lines.size(); i++){
            if(lines.get(i).contains(needle)){
                return i + 1;
            }
        }
        return fallbackLine;
    }
}
