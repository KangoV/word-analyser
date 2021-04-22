package org.belldj.words.internal;

import org.belldj.words.api.AnalysisException;
import org.belldj.words.internal.StandardWordAnalyser;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class StandardWordAnalyserTest {

    private static final String NL = "\n";

    @Test
    void testFileNotFound() throws Exception {
        assertThatExceptionOfType(AnalysisException.class).isThrownBy(() -> {
            var uri = new URI("does_not_exist.txt");
            new StandardWordAnalyser().analyseUri(uri);
        }).withMessage("File not found: does_not_exist.txt");
    }

    @Test
    void testNullUri() throws Exception {
        assertThatExceptionOfType(AnalysisException.class).isThrownBy(() -> {
            new StandardWordAnalyser().analyseUri(null);
        }).withMessage("No URI provided");
    }

    @Test
    void testAnalyseTextShort() throws Exception {
        var uri = ClassLoader.getSystemResource("short_1.txt").toURI();
        var result = new StandardWordAnalyser().analyseUri(uri);
        assertThat(result).isNotNull();
        assertThat(result.getCount()).isEqualTo(9L);
        assertThat(result.getAverageLength()).isEqualByComparingTo(new BigDecimal("4.556"));
        assertThat(result.getMostFrequentlyOcurringLength()).isEqualTo(2);
        assertThat(result.getMostFrequentWordlengths()).containsOnly(4, 5);

        assertThat(result.print()).isEqualTo(
            "Word count = 9" + NL +
            "Average word length = 4.556" + NL +
            "Number of words of length 1 is 1" + NL +
            "Number of words of length 2 is 1" + NL +
            "Number of words of length 3 is 1" + NL +
            "Number of words of length 4 is 2" + NL +
            "Number of words of length 5 is 2" + NL +
            "Number of words of length 7 is 1" + NL +
            "Number of words of length 10 is 1" + NL +
            "The most frequently occurring word length is 2, for word lengths of 4 & 5" + NL
            );
    }

    @Test
    void testShort2() throws Exception {
        var uri = ClassLoader.getSystemResource("short_2.txt").toURI();
        var result = new StandardWordAnalyser().analyseUri(uri);
        assertThat(result).isNotNull();
        assertThat(result.getCount()).isEqualTo(13L);
        assertThat(result.getAverageLength()).isEqualByComparingTo(new BigDecimal("4.923"));
        assertThat(result.getMostFrequentlyOcurringLength()).isEqualTo(3);
        assertThat(result.getMostFrequentWordlengths()).containsOnly(4, 5, 7);

        assertThat(result.print()).isEqualTo(
            "Word count = 13" + NL +
            "Average word length = 4.923" + NL +
            "Number of words of length 1 is 1" + NL +
            "Number of words of length 2 is 1" + NL +
            "Number of words of length 3 is 1" + NL +
            "Number of words of length 4 is 3" + NL +
            "Number of words of length 5 is 3" + NL +
            "Number of words of length 7 is 3" + NL +
            "Number of words of length 10 is 1" + NL +
            "The most frequently occurring word length is 3, for word lengths of 4, 5 & 7" + NL
            );
    }

    @Test
    void testShortSpecials() throws Exception {
        var uri = ClassLoader.getSystemResource("short_3_specials.txt").toURI();
        var result = new StandardWordAnalyser().analyseUri(uri);
        assertThat(result).isNotNull();
        assertThat(result.getCount()).isEqualTo(9L);
        assertThat(result.getAverageLength()).isEqualTo("4.556");
        assertThat(result.getMostFrequentlyOcurringLength()).isEqualTo(2);
        assertThat(result.getMostFrequentWordlengths()).containsOnly(4, 5);
    }

    @Test
    void testLong() throws Exception {
        var uri = ClassLoader.getSystemResource("bible_daily.txt").toURI();
        var result = new StandardWordAnalyser().analyseUri(uri);
        assertThat(result).isNotNull();
        assertThat(result.getCount()).isEqualTo(792270L);
    }

}
