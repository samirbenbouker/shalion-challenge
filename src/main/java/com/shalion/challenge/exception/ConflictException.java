package com.shalion.challenge.exception;

public class ConflictException extends RuntimeException {

    /**
     * Creates a conflict exception with a custom message.
     *
     * @param message error message
     */
    public ConflictException(String message) {
        super(message);
    }
}
