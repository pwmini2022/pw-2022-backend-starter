package pw.react.backend.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice(annotations = RestController.class)
@Slf4j
public class CompanyControllerExceptionHelper {

    @ExceptionHandler(value = { InvalidFileException.class })
    public ResponseEntity<ExceptionDetails> handleInvalidFileException(InvalidFileException ex) {
        log.error("Invalid Input Exception: {}", ex.getMessage());
        return new ResponseEntity<>(new ExceptionDetails(HttpStatus.NOT_FOUND, ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = { ResourceNotFoundException.class })
    public ResponseEntity<ExceptionDetails> handleResourceNotFoundException(ResourceNotFoundException ex) {
        log.error("Invalid Input Exception: {}", ex.getMessage());
        return new ResponseEntity<>(new ExceptionDetails(HttpStatus.NOT_FOUND, ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = { UnauthorizedException.class })
    public ResponseEntity<ExceptionDetails> handleUnauthorizedException(UnauthorizedException ex) {
        log.error("Invalid Input Exception: {}", ex.getMessage());
        return new ResponseEntity<>(new ExceptionDetails(HttpStatus.UNAUTHORIZED, ex.getMessage()), HttpStatus.UNAUTHORIZED);
    }

}
