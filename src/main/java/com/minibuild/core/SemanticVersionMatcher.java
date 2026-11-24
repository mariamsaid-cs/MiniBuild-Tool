package com.minibuild.core;

import java.util.List;
import java.util.ArrayList;

public class SemanticVersionMatcher implements VersionMatcher{
    @Override
    public int compareVersions(String versionA, String versionB){
        List<Integer> a = parseParts(versionA);
        List<Integer> b = parseParts(versionB);
        int maxLen = Math.max(a.size(), b.size());

        for(int i = 0; i < maxLen; i++){
            int partA = i < a.size() ? a.get(i) : 0;
            int partB = i < b.size() ? b.get(i) : 0;
            if(partA != partB){
                return Integer.compare(partA, partB);
            }
        }
        return 0;
    }

    @Override
    public boolean matchesRange(String version, String versionOrRange) {
        String spec = versionOrRange.trim();
        boolean startsBracket = spec.startsWith("[");
        boolean startsParen = spec.startsWith("(");
        boolean endsBracket = spec.endsWith("]");
        boolean endsParen = spec.endsWith(")");

        if((startsBracket || startsParen) && (endsBracket || endsParen)){
            String inner = spec.substring(1, spec.length() - 1);
            String[] bounds = inner.split(",", -1);
            if(bounds.length != 2){
                throw new IllegalArgumentException("Invalid version range: " + versionOrRange);
            }

            String lower = bounds[0].trim();
            String upper = bounds[1].trim();

            if(!lower.isEmpty()){
                int cmpLower = compareVersions(version, lower);
                if(startsBracket && cmpLower < 0) return false;
                if(startsParen && cmpLower <= 0) return false;
            }

            if(!upper.isEmpty()){
                int cmpUpper = compareVersions(version, upper);
                if(endsBracket && cmpUpper > 0) return false;
                if(endsParen && cmpUpper >= 0) return false;
            }
            return true;
        }
        return compareVersions(version, spec) == 0;
    }

    private List<Integer> parseParts(String rawVersion){
        if(rawVersion == null || rawVersion.isBlank()){
            throw new IllegalArgumentException("Version cannot be blank");
        }
        String[] tokens = rawVersion.trim().split("\\.");
        List<Integer> parts = new ArrayList<>(tokens.length);
        for(String token : tokens){
            if(!token.matches("\\d+")){
                throw new IllegalArgumentException("Invalid version segment'" + token + "' in'" + rawVersion + "'");
            }
            parts.add(Integer.parseInt(token));
        }
        return parts;
    }
}
