/*
 * Copyright 2018-2021-04-21 Crown Copyright
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.belldj.words.internal;

import org.belldj.words.api.AnalysisException;
import org.belldj.words.api.AnalysisResult;
import org.belldj.words.api.WordAnalyser;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * The standard implementation of {@link WordAnalyser} which loads the target
 * file into memory and then processes it.
 *
 * @since 1.0.0
 */
public class StandardWordAnalyser implements WordAnalyser {

    @Override
    public AnalysisResult analyseUri(final URI uri) {
        if (uri == null) {
            throw new AnalysisException("No URI provided");
        }
        return getPath(uri)
            .map(StandardWordAnalyser::load)
            .map(StandardWordAnalyser::split)
            .map(StandardWordAnalyser::analyse)
            .orElseThrow(() -> new AnalysisException("Unknown error occurred trying to load \"" + uri));
    }

    private static Optional<Path> getPath(final URI uri) {
        Path path = null;
        try {
            path = Paths.get(uri);
        } catch (Exception e) {
            throw new AnalysisException("File not found: " + uri, e);
        }
        return Optional.ofNullable(path);
    }

    private static String load(final Path path) {
        try {
            return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new AnalysisException("Failed to load \"" + path.getFileName() + "\": " + e.getMessage(), e);
        }
    }

    private static List<String> split(final String input) {
        var wordArray = input.split("\\s+");
        return List.of(wordArray);
    }

    private static AnalysisResult analyse(final List<String> words) {

        var pattern = Pattern.compile("[-+.^:?,=()]");
        var lengthCounts = new HashMap<Integer, Integer>();
        var count = 0;
        var length = 0;

        for (String string : words) {
            // remove unwanted punctuation
            var word = pattern.matcher(string).replaceAll("");
            // now test if we have a number (with formatting now removed)
            var isNumeric = word.chars().allMatch(Character::isDigit);
            // if we do not have a numeric number we can count it
            if (!isNumeric) {
                count++;
                length += word.length();
                lengthCounts.merge(word.length(), 1, (prev, one) -> prev + one);
            }
        }

        var average = new BigDecimal(length).divide(new BigDecimal(count), 3, RoundingMode.HALF_UP);

        var result = new AnalysisResult.Builder()
            .count(count)
            .averageLength(average)
            .wordsOfLength(lengthCounts)
            .build();

        return result;

    }

}
