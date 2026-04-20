package com.shalion.challenge.exception;

public class NotFoundException extends RuntimeException {

    /**
     * Creates a not-found exception with a custom message.
     *
     * @param message error message
     */
    public NotFoundException(String message) {
        super(message);
    }
}
