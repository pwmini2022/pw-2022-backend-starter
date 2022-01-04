package pw.react.backend.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice(annotations = RestController.class)
@Slf4j
public class ControllerExceptionHelper {

    @ExceptionHandler(value = { InvalidFileException.class, ResourceNotFoundException.class })
    public ResponseEntity<ExceptionDetails> handleInvalidFileException(InvalidFileException ex) {
        log.error("Invalid Input Exception: {}", ex.getMessage());
        return new ResponseEntity<>(new ExceptionDetails(HttpStatus.NOT_FOUND, ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = { UnauthorizedException.class })
    public ResponseEntity<ExceptionDetails> handleUnauthorizedException(UnauthorizedException ex) {
        log.error("Invalid Input Exception: {}", ex.getMessage());
        return new ResponseEntity<>(new ExceptionDetails(HttpStatus.UNAUTHORIZED, ex.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = { UserValidationException.class, UsernameNotFoundException.class })
    public ResponseEntity<ExceptionDetails> handleUnauthorizedException(UserValidationException ex) {
        log.error("Invalid User Exception: {}", ex.getMessage());
        return new ResponseEntity<>(new ExceptionDetails(HttpStatus.BAD_REQUEST, ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

}
