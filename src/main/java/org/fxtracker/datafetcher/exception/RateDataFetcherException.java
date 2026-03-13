package org.fxtracker.datafetcher.exception;

public class RateDataFetcherException extends Exception {

    public RateDataFetcherException(String message) {
        super(message);
    }

    public RateDataFetcherException(String message, Throwable cause) {
        super(message, cause);
    }
}
