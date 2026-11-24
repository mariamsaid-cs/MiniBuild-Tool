package com.minibuild.core;

import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class DescriptorAndVersionTddTest {

    // Replace these with your concrete implementations once you write them
    private final VersionMatcher versionMatcher = new SemanticVersionMatcher();
    private final DescriptorParser parser = new JsonDescriptorParser();

    @ParameterizedTest(name = "compare {0} vs {1} -> {2}")
    @CsvSource({
            "1.2.0, 1.10.0, NEGATIVE",
            "1.10.0, 1.2.0, POSITIVE",
            "1.0.0, 1.0, ZERO",
            "2.0.0, 1.9.9, POSITIVE"
    })
    void shouldCompareVersionsNumerically(String v1, String v2, String expectedSign) {
        int cmp = versionMatcher.compareVersions(v1, v2);
        switch (expectedSign) {
            case "NEGATIVE" -> assertTrue(cmp < 0, () -> v1 + " should be < " + v2);
            case "POSITIVE" -> assertTrue(cmp > 0, () -> v1 + " should be > " + v2);
            case "ZERO" -> assertEquals(0, cmp, () -> v1 + " should equal " + v2);
            default -> fail("Unknown sign: " + expectedSign);
        }
    }

    @ParameterizedTest(name = "version {0} matches range {1} -> {2}")
    @CsvSource({
            "1.0.0, '[1.0,2.0)', true",
            "1.5.2, '[1.0,2.0)', true",
            "1.9.9, '[1.0,2.0)', true",
            "2.0.0, '[1.0,2.0)', false",
            "0.9.9, '[1.0,2.0)', false",
            "1.2.0, '1.2.0', true",
            "1.2.1, '1.2.0', false"
    })
    void shouldMatchVersionRanges(String version, String range, boolean expected) {
        assertEquals(expected, versionMatcher.matchesRange(version, range));
    }

    @ParameterizedTest(name = "invalid descriptor line {1}: {2}")
    @CsvSource(
            delimiter = '|',
            value = {
                    "{\"projectName\": \"demo\",\n\"modules\": []}|2|Missing required field: localRepositoryDir",
                    "{\"projectName\": \"demo\",\n\"localRepositoryDir\": \"./repo\",\n\"modules\": [{\"name\": \"app\", \"version\": \"not-a-version\"}]}|3|Invalid version"
            }
    )
    void shouldThrowExceptionWithAccurateLineNumber(
            String jsonContent,
            int expectedLine,
            String expectedMessagePart,
            @TempDir Path tempDir
    ) throws Exception {
        Path file = tempDir.resolve("minibuild.json");
        Files.writeString(file, jsonContent);

        DescriptorParseException ex = assertThrows(
                DescriptorParseException.class,
                () -> parser.parse(file)
        );
        assertEquals(expectedLine, ex.getLineNumber());
        assertTrue(ex.getMessage().contains(expectedMessagePart));
    }
}