package org.belldj.words.api;

/**
 * Exception thrown for any error that occurs during analysis
 */
public class AnalysisException extends RuntimeException {

    /**
     * @see RuntimeException#RuntimeException(String, Throwable)
     */
    public AnalysisException(final String message, final Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

    /**
     * @see RuntimeException#RuntimeException(String)
     */
    public AnalysisException(final String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

}
