package pw.react.backend.exceptions;

import lombok.Getter;

public class UnauthorizedException extends RuntimeException {
    @Getter
    private final String path;

    public UnauthorizedException(String message, String path) {
        super(message);
        this.path = path;
    }
}
