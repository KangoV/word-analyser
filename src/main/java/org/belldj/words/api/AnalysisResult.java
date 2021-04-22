package org.belldj.words.api;

import org.belldj.words.internal.ImmutableStyle;
import org.belldj.words.internal.StandardWordAnalyser;
import org.immutables.value.Value;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Provides service and configuration for a resource client
 *
 * @since 0.5.0
 */
@Value.Immutable
@ImmutableStyle
public interface AnalysisResult {

    /**
     * Exposes the generated builder outside this package
     * <p>
     * While the generated implementation (and consequently its builder) is not
     * visible outside of this package. This builder inherits and exposes all public
     * methods defined on the generated implementation's Builder class.
     */
    class Builder extends ImmutableAnalysisResult.Builder { // empty
    }

    /**
     * Returns the number of words counted. This number can vary between
     * implementations. For example the {@link StandardWordAnalyser} removes
     * formatted numbers from the count.
     *
     * @return the number of words counted
     */
    long getCount();

    /**
     * Returns the average word length
     *
     * @return the average word length
     * @see #print() for an example
     */
    BigDecimal getAverageLength();

    /**
     * Returns the count of the number of words at each length. The key is the word
     * length and the value is the number of words encountered at that length
     *
     * @return the count of the number of words at each length
     * @see #print() for an example
     */
    Map<Integer, Integer> getWordsOfLength();

    /**
     * Returns the most frequently occurring word length
     *
     * @return the most frequently occurring word length
     * @see #print() for an example
     */
    @Value.Derived
    default int getMostFrequentlyOcurringLength() {
        return getWordsOfLength().values().stream().mapToInt(v -> v).max().orElse(0);
    }

    /**
     * Returns a list of most frequent word lengths
     *
     * @return a list of most frequent word lengths
     * @see #print() for an example
     */
    @Value.Derived
    default List<Integer> getMostFrequentWordlengths() {
        var len = getMostFrequentlyOcurringLength();
        return getWordsOfLength().entrySet().stream()
            .filter(e -> e.getValue() == len)
            .map(e -> e.getKey())
            .collect(Collectors.toList());
    }

    /**
     * Returns a string containing the formatted output represented by this
     * {@code AnalysisResult}. For example given the words:
     *
     * <pre>
     * Hello world & good morning. The date is 18/05/2016
     * </pre>
     * The following output would be returned as a string:
     * <pre>
     * Word count = 9
     * Average word length = 4.556
     * Number of words of length 1 is 1
     * Number of words of length 2 is 1
     * Number of words of length 3 is 1
     * Number of words of length 4 is 2
     * Number of words of length 5 is 2
     * Number of words of length 7 is 1
     * Number of words of length 10 is 1
     * The most frequently occurring word length is 2, for word lengths of 4 & 5
     * </pre>
     *
     * @return
     */
    default String print() {

        var output = new StringBuilder();

        println(output, "Word count = %s", getCount());
        println(output, "Average word length = " + getAverageLength());

        getWordsOfLength().entrySet().forEach(entry -> {
            println(output, "Number of words of length %s is %s", entry.getKey(), entry.getValue());
        });

        var lengths = new StringBuilder();
        var mostfreqLens = getMostFrequentWordlengths();
        if (mostfreqLens.size() > 1) {
            for (int i = 0; i < (mostfreqLens.size() - 1); i++) {
                if (i > 0) {
                    lengths.append(", ");
                }
                lengths.append(mostfreqLens.get(i));
            }
            lengths.append(" & ");
        }
        lengths.append(mostfreqLens.get(mostfreqLens.size() - 1));

        println(output, "The most frequently occurring word length is %s, for word lengths of %s",
            getMostFrequentlyOcurringLength(), lengths.toString());

        return output.toString();
    }

    private static void println(final StringBuilder output, final String template, final Object... args) {
        if (args != null && args.length > 0) {
            output.append(String.format(template, args));
        } else {
            output.append(template);
        }
        output.append("\n");
    }

}
