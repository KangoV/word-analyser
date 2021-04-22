package org.belldj.words.api;

import java.net.URI;

/**
 * An analyser process a series of words and provides a result containing
 * various metrics such as total number of words, the average word length, the
 * most frequently occurring word length, and a list of the number of words of
 * each length.
 *
 * @since 1.0.0
 */
public interface WordAnalyser {

    /**
     * Returns the result of running the analysis on the contents pointed to by the
     * provided URI
     *
     * @param uri The uri whos contents are to be processed
     * @return the results
     * @throws AnalysisException if an error occurs
     */
    AnalysisResult analyseUri(URI uri);

}
