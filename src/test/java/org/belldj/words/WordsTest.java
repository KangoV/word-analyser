package org.belldj.words;

import org.junit.jupiter.api.Test;
import picocli.CommandLine;

import static org.assertj.core.api.Assertions.assertThat;

class WordsTest {

    @Test
    void testCommand() {
        int exitCode = new CommandLine(new Words()).execute("src/test/resources/short_1.txt");
        assertThat(exitCode).isEqualTo(0);
    }

}
