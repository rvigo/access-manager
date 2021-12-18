package br.com.rvigo.accessmanager.security.exceptions;

import lombok.Getter;

@Getter
public class NoSuchUserException extends RuntimeException {
    private final String message;
    private static final String DEFAULT_MESSAGE = "user not found";

    public NoSuchUserException() {
        super(DEFAULT_MESSAGE);
        message = DEFAULT_MESSAGE;
    }

    public NoSuchUserException(String message) {
        super(message);
        this.message = message;
    }
}
