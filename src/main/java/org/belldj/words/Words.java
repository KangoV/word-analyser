package org.belldj.words;

import org.belldj.words.api.AnalysisException;
import org.belldj.words.internal.StandardWordAnalyser;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.concurrent.Callable;

/**
 * The words command line interface specification. This example implements
 * {@code Callable}, so parsing, error handling and handling user requests for
 * usage help or version help can be done with one line of code.
 *
 * @since 1.0.0
 */
@Command(
    name = "words",
    mixinStandardHelpOptions = true,
    version = "words 1.0.0-SNAPSHOT",
        description = "read the contents of a plain text file and enable the display of the "
            + "total number of words, the average word length, the most frequently occurring word length, and a "
        + "list of the number of words of each length.")
class Words implements Callable<Integer> {

    @Parameters(index = "0", description = "The file containing the words to count.")
    private Path path;

    @Override
    public Integer call() throws Exception {
        var rc = 0;
        try {
            var uri = path.toUri();
            var result = new StandardWordAnalyser().analyseUri(uri);
            var output = result.print();
            System.out.printf(output);
        } catch (AnalysisException e) {
            var cause = e.getCause();
            if (cause == null) {
                System.out.printf("Processing failed: %s", e.getMessage());
            } else if (cause instanceof FileNotFoundException) {
                System.out.printf("File not found: %s", cause.getMessage());
            } else {
                System.out.printf("Unknown error occurred: %s", cause.getMessage());
            }
            rc = 1;
        }
        return rc;
    }

    /**
     * The Words CLI entry point
     *
     * @param args The program arguments
     */
    public static void main(final String... args) {
        int exitCode = new CommandLine(new Words()).execute(args);
        System.exit(exitCode);
    }

}