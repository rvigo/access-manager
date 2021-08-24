package br.com.rvigo.accessmanager.security.exceptions;

import lombok.Getter;

@Getter
public class JwtTokenException extends RuntimeException {
    private final Throwable cause;
    private final String message;

    public JwtTokenException() {
        super();
        this.message = null;
        this.cause = null;
    }

    public JwtTokenException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
        this.cause = cause;
    }
}
