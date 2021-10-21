package pw.react.backend.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class ExceptionDetails {
    private final HttpStatus status;
    private final String errorMessage;
}
